/* LIME 1.1 - A middleware for coordination of mobile agents and mobile hosts
 * Copyright (C) 2003,
 * Chien-Liang Fok, Christine Julien, Radu Handorean, Rohan Sen, Tom Elgin,
 * Amy L. Murphy, Gian Pietro Picco, and Gruia-Catalin Roman
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package chat;


/**
 * Encapsulation of a chat message item
 *
 * @version November 29, 1999
 * @author Bryan D. Payne
 */

public class LChatMessage implements java.io.Serializable{
    
    /**
     * Create a chat message from the given info
     *
     * @param id The userid for this message
     * @param message The message
     */
    public LChatMessage (String id, String message, boolean boldOn,
			 boolean italicizeOn, boolean underlineOn,
			 java.awt.Color color){
	this.id = id;
	this.message = message;
	this.boldOn = boldOn;
	this.italicizeOn = italicizeOn;
	this.underlineOn = underlineOn;
	this.color = color;
    }

    /**
     * Accessor for the ID
     *
     * @return The ID
     */
    public String getID (){
	return id;
    }

    /**
     * Accessor for the message
     *
     * @return The message
     */
    public String getMessage (){
	return message;
    }

    /**
     * Specifies how this message should be displayed
     *
     * @return The message in a printable format
     */
    public String toString (){
	String result = "";

	result += "<b>"+id+":</b> ";
	if (boldOn) result += "<b>";
	if (italicizeOn) result += "<i>";
	if (underlineOn) result += "<u>";
	result += message;
	if (underlineOn) result += "</u>";
	if (italicizeOn) result += "</i>";
	if (boldOn) result += "</b>";
	result += "<br>";

	return result;
    }

    // Data carrying variables
    private String id;
    private String message;

    // Message properties
    private boolean boldOn;
    private boolean italicizeOn;
    private boolean underlineOn;
    private java.awt.Color color;
}
