/*

   Milenia Grafter Server
  
   Copyright (c) 2007-2008 by Milan Toth. All rights reserved.
  
   This program is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public License
   of the License, or (at your option) any later version.
  
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
  
   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundataion, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA. 
   
*/ 

package com.milgra.server.api;

/**
	
	StatusEvent class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316

	Tasks of StatusEvent
	
		- hold informations related to a status event

**/

import com.milgra.server.Library;

public class StatusEvent 
{
	
	public static final String FAILURE = Library.FAILURE;
	public static final String CLOSURE = Library.CLOSURE;
	public static final String SUCCESS = Library.SUCCESS;
	public static final String REJECTION = Library.REJECTION;

	public static final String PLAYSTART = Library.PLAYSTART;
	public static final String PLAYRESET = Library.PLAYRESET;
	public static final String PLAYFAILED = Library.PLAYFAILED;
	public static final String PUBLISHSTART = Library.PUBLISHSTART;
	public static final String RECORDNOACCESS = Library.RECORDNOACCESS;
	public static final String UNPUBLISHSUCCESS = Library.UNPUBLISHSUCCESS;

	public String code;
	public Client client;
	public WrapperMap info;
	
	public StatusEvent ( WrapperMap infoX , Client clientX )
	{
		
		info = infoX;
		code = infoX.getString( "code" );
		client = clientX;
		
	}

}
