package com.hoodyoodoo.droidapp.model;

/**
 * Celebrity class is the object model for the Celebrity object that interacts with the back-end as a 
 * resource Objecttype that can be persisted in one or more Collections, in this case the Celebrity
 * Collection and also the TopCelebrity Collection. 
 * <p>
 * Note, this class is an example a complex object with image data represented as a byte array can be persisted 
 * with a single line of code in the client application using the FatFractal Emergent Framework.
 *  
 */
public class Celebrity {
	/** The String that contains the First Name for this Celebrity */
	private String m_firstName = null;
	/** The String that contains the Last Name for this Celebrity */
	private String m_lastName = null;
	/** The byte[] for the profile image for this Celebrity */
	private byte[] m_imageData = null;
	/** The String that contains the gender (male or female) for this Celebrity */
	private String m_gender = null;
	/** The Integer that contains the total times the Celebrity was selected.*/
	private Integer m_selectedCount = 0;
	/** The Integer that contains the total times the Celebrity was rejected.*/
	private Integer m_rejectedCount = 0;

	/** Class constructor. */
	public
	Celebrity() {}

	/** getter for the {@link #m_firstName} parameter*/
	public String getFirstName()      {return m_firstName;}
	/** getter for the {@link #m_lastName} parameter*/
	public String getLastName()       {return m_lastName;}
	/** getter for the {@link #m_imageData} parameter*/
	public byte[] getImageData()      {return m_imageData;}
	/** getter for the {@link #m_gender} parameter*/
	public String getGender()         {return m_gender;}
	/** getter for the {@link #m_selectedCount} parameter*/
	public Integer getSelectedCount() {return m_selectedCount;}
	/** getter for the {@link #m_rejectedCount} parameter*/
	public Integer getRejectedCount() {return m_rejectedCount;}	   

	/** setter for the {@link #m_firstName} parameter*/
	public void setFirstName(String param)      {m_firstName = param;}
	/** setter for the {@link #m_lastName} parameter*/
	public void setLastName(String param)       {m_lastName = param;}
	/** setter for the {@link #m_imageData} parameter*/
	public void setImageData(byte[] param)      {m_imageData = param;}
	/** setter for the {@link #m_gender} parameter*/
	public void setGender(String param)         {m_gender= param;}
	/** setter for the {@link #m_selectedCount} parameter*/
	public void setSelectedCount(Integer param) {m_selectedCount = param;}
	/** setter for the {@link #m_rejectedCount} parameter*/
	public void setRejectedCount(Integer param) {m_rejectedCount = param;}
}