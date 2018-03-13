package com.hassan.masla7ty.chat;

import java.util.Date;


/**
 * The Class Conversation is a Java Bean class that represents a single chat
 * conversation message.
 */
public class Conversation
{


	/** The msg. */
	private String msg;

	/** The date. */
	private Date date;

	/** The receiver. */
	private String sender;

	/**
	 * Instantiates a new conversation.
	 *
	 * @param msg
	 *            the msg
	 * @param date
	 *            the date
	 * @param sender
	 *            the receiver
	 */
	public Conversation(String msg, Date date, String sender)
	{
		this.msg = msg;
		this.date = date;
		this.sender = sender;
	}

	/**
	 * Instantiates a new conversation.
	 */
	public Conversation()
	{
	}

	/**
	 * Gets the msg.
	 * 
	 * @return the msg
	 */
	public String getMsg()
	{
		return msg;
	}

	/**
	 * Sets the msg.
	 * 
	 * @param msg
	 *            the new msg
	 */
	public void setMsg(String msg)
	{
		this.msg = msg;
	}



	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public Date getDate()
	{
		return date;
	}

	/**
	 * Sets the date.
	 * 
	 * @param date
	 *            the new date
	 */
	public void setDate(Date date)
	{
		this.date = date;
	}

	/**
	 * Gets the receiver.
	 * 
	 * @return the receiver
	 */
	public String getSender()
	{
		return sender;
	}

	/**
	 * Sets the receiver.
	 * 
	 * @param sender
	 *            the new receiver
	 */
	public void setSender(String sender)
	{
		this.sender = sender;
	}



}
