package com.hoodyoodoo.droidapp.model;

/**
 * StatsObject class is the object model for the StatsObject object that interacts with the back-end 
 * <p>
 * Note, this class is an example an object that is not persisted on the back-end, but instead uses a 
 * Server Extension to create and populate the object on the fly. 
 */
public class StatsObject {
	/** The Integer that contains the total times the Celebrity was selected.*/
	private Integer m_totalUsers = 0;
	/** The Integer that contains the total times the Celebrity was rejected.*/
	private Integer m_totalCelebrities = 0;
	/** The Integer that contains the total times the Celebrity was selected.*/
	private Integer m_totalRatings = 0;
	/** The Integer that contains the total times the Celebrity was rejected.*/
	private Integer m_yourRatings = 0;

	/** Class constructor. */
	public
	StatsObject() {}

	/** getter for the {@link #m_totalUsers} parameter*/
	public Integer getTotalUsers() {return m_totalUsers;}
	/** getter for the {@link #m_totalCelebrities} parameter*/
	public Integer getTotalCelebrities() {return m_totalCelebrities;}	   
	/** getter for the {@link #m_totalRatings} parameter*/
	public Integer getTotalRatings() {return m_totalRatings;}
	/** getter for the {@link #m_yourRatings} parameter*/
	public Integer getYourRatings() {return m_yourRatings;}	   

	/** setter for the {@link #m_totalUsers} parameter*/
	public void setTotalUsers(Integer param) {m_totalUsers = param;}
	/** setter for the {@link #m_totalCelebrities} parameter*/
	public void setTotalCelebrities(Integer param) {m_totalCelebrities = param;}
	/** setter for the {@link #m_totalRatings} parameter*/
	public void setTotalRatings(Integer param) {m_totalRatings = param;}
	/** setter for the {@link #m_yourRatings} parameter*/
	public void setYourRatings(Integer param) {m_yourRatings = param;}
}
