package com.cogcong.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.cogcong.mongo.MongoFacade;

public class Legislator {

	private MongoFacade facade = MongoFacade.getInstance();
	private Document legislatorDoc;
	private Document legislatorStats;
	private String bioguide_id;
	
	public Legislator(String bioguide_id){
		this.setBioguide_id(bioguide_id);
		legislatorDoc = facade.getLegislatorByBioID(bioguide_id);
		legislatorStats = facade.getLegislatorStatsByBioID(bioguide_id);
		if(legislatorStats == null){
			legislatorStats = new Document("bioguide_id", bioguide_id);
			facade.db.getCollection("LegislatorStats").insertOne(legislatorStats);
			legislatorStats = facade.getLegislatorStatsByBioID(bioguide_id);
		}
	}
	
	public Legislator(Document legislatorDoc){
		this.legislatorDoc = legislatorDoc;
		bioguide_id = ((Document) legislatorDoc.get("id")).getString("bioguide");
		legislatorStats = facade.getLegislatorStatsByBioID(bioguide_id);
		if(legislatorStats == null){
			legislatorStats = new Document("bioguide_id", bioguide_id);
			facade.db.getCollection("LegislatorStats").insertOne(legislatorStats);
			legislatorStats = facade.getLegislatorStatsByBioID(bioguide_id);
		}
	}
	
	public List<String> getMainSponsoredBills(){
		Object sObj = getDoc().get("main_sponsored_bills");
		if(sObj instanceof List<?>){
			return (List<String>) sObj;
		}
		return new ArrayList<>();
	}
	
	
	public List<String> getSponsoredBills(){
		Object sObj = getDoc().get("sponsored_bills");
		if(sObj instanceof List<?>){
			return (List<String>) sObj;
		}
		return null;
	}
	
	public void updateKeywords(Map<String, Integer> keywords){
		legislatorStats.remove("keywords");
		legislatorStats.append("keywords", keywords);
		update();
	}
	
	public void updateSubjects(Map<String, Integer> subjects){
		legislatorStats.remove("subjects");
		legislatorStats.append("subjects", subjects);
		update();
	}
	
	public Integer getKeywordCount(String keyword){
		return getKeywords().get(keyword);
	}
	
	public Integer getSubjectCount(String subject){
		return getSubjects().get(subject);
	}
	
	public List<String> getTopNKeywords(int n){
		Map<String, Integer> keywords = getKeywords();
		if(keywords == null){
			return new ArrayList<>();
		}
		List<String> words = new ArrayList<>(keywords.keySet());
		Collections.sort(words, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return keywords.get(o2).compareTo(keywords.get(o1));
			}
		});
		List<String> topWords = new ArrayList<>();
		for(int i = 0; i < n && (i+1) < words.size(); i++){
			topWords.add(words.get(i));
		}
		return topWords;
	}
	
	public List<String> getTopNSubjects(int n){
		Map<String, Integer> subjects = getSubjects();
		if(subjects == null){
			return null;
		}
		List<String> subjectsList = new ArrayList<>(subjects.keySet());
		Collections.sort(subjectsList, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return subjects.get(o2).compareTo(subjects.get(o1));
			}
		});
		List<String> topSubjects = new ArrayList<>();
		for(int i = 0; i < n && (i+1) < subjectsList.size(); i++){
			topSubjects.add(subjectsList.get(i));
		}
		return topSubjects;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Integer> getKeywords(){
		
		Document keywordsDoc = (Document) legislatorStats.get("keywords");
		if(keywordsDoc == null){
			return null;
		}
		Map<String, Integer> keywords = new HashMap<>();
		keywords.putAll((Map) keywordsDoc); 
		return keywords;

	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Integer> getSubjects(){
		
		Document subjectsDoc = (Document) legislatorStats.get("subjects");
		if(subjectsDoc == null){
			return null;
		}
		Map<String, Integer> subjects = new HashMap<>();
		subjects.putAll((Map) subjectsDoc); 
		return subjects;

	}
	
	public Date getLastTermDate(){
		@SuppressWarnings("unchecked")
		List<Document> terms = (List<Document>) getDoc().get("terms");
		Document term = terms.get(terms.size()-1);
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(term.getString("end"));
		} catch (ParseException e) {
			return null;
		}
	}
	
	public String getLatestParty(){
		@SuppressWarnings("unchecked")
		List<Document> terms = (List<Document>) getDoc().get("terms");
		Document term = terms.get(terms.size()-1);
		String party = term.getString("party");
		return party;
	}
	
	public String getLatestPartySymbol(){
		@SuppressWarnings("unchecked")
		List<Document> terms = (List<Document>) getDoc().get("terms");
		Document term = terms.get(terms.size()-1);
		String party = term.getString("party");
		return "(" + party.substring(0,1) + ")";
	}
	
	public String getName(){
		Document name = (Document) getDoc().get("name");
		return name.getString("first") + " " + name.getString("last");
	}
	
	public String getLastName(){
		Document name = (Document) getDoc().get("name");
		return name.getString("last");
	}
	
	public Date getBirthday(){
		Document bio = (Document) getDoc().get("bio");
		try {
			return  new SimpleDateFormat("yyyy-MM-dd").parse(bio.getString("birthday"));
		} catch (ParseException e) {
			return null;
		}
	}

	public String getGender(){
		Document bio = (Document) getDoc().get("bio");
		return bio.getString("gender");
	}
	
	public String getBioguide_id() {
		return bioguide_id;
	}

	public void setBioguide_id(String bioguide_id) {
		this.bioguide_id = bioguide_id;
	}
	
	public Document getDoc(){
		return legislatorDoc;
	}
	
	private void update(){
		facade.db.getCollection("Legislators").replaceOne(
				new Document("_id", getDoc().get("_id")), getDoc());
		facade.db.getCollection("LegislatorStats").replaceOne(
				new Document("_id", legislatorStats.get("_id")), legislatorStats);
	}
	
}
