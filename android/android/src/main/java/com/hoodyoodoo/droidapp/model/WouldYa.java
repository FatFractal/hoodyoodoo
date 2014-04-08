package com.hoodyoodoo.droidapp.model;

/**
 * WouldYa class is the object model for the WouldYa object that interacts with the back-end as a resource 
 * Objecttype that can be persisted in one or more Collections, in this case the WouldYa Collection.
 */
public class WouldYa {
	/** The String that contains the guid of the selected Celebrity for this WouldYa */
	private String m_selectedGuid = null;
	/** The String that contains the guid of the rejected Celebrity for this WouldYa */
	private String m_rejectedGuid = null;

	/** Class constructor. */
	public
	WouldYa() {}

	/** getter for the {@link #m_selectedGuid} parameter*/
	public String getSelectedGuid()           {return m_selectedGuid;}
	/** getter for the {@link #m_rejectedGuid} parameter*/
	public String getRejectedGuid()           {return m_rejectedGuid;}

	/** setter for the {@link #m_selectedGuid} parameter*/
	public void setSelectedGuid(String param) {m_selectedGuid = param;}
	/** setter for the {@link #m_rejectedGuid} parameter*/
	public void setRejectedGuid(String param) {m_rejectedGuid = param;}
}