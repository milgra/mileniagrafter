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
	
	Wrapper class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316

	Tasks of Wrapper
	
		- wrapes various data types which is used by amf encoder/decoder
 
**/

public class Wrapper 
{
	
	public static final String MAP = "map";
	public static final String NULL = "null";
	public static final String LIST = "list";
	public static final String DOUBLE = "double";
	public static final String STRING = "string";
	public static final String BOOLEAN = "boolean";

	public String type;

	public double doubleValue;
	public String stringValue;
	public boolean booleanValue;
	public WrapperMap mapValue;
	public WrapperList listValue;
	
	
	public Wrapper ( )
	{
	
		type = NULL;
		
	}	
	
	
	public Wrapper ( WrapperMap valueX )
	{
		
		type = MAP;
		mapValue = valueX;

	}	
	
	
	public Wrapper ( WrapperList valueX )
	{
		
		type = LIST;
		listValue = valueX;

	}	
	
	
	public Wrapper ( double valueX )
	{
		
		type = DOUBLE;
		doubleValue = valueX;

	}
	
	
	public Wrapper ( String valueX )
	{

		type = STRING;
		stringValue = valueX;

	}
	
	
	public Wrapper ( boolean valueX )
	{
		
		type = BOOLEAN;
		booleanValue = valueX;

	}
	
	public String toString ( )
	{
		
		if ( type == NULL ) return "null";
		if ( type == MAP ) return "map";
		if ( type == LIST ) return "list";
		if ( type == DOUBLE ) return doubleValue + "";
		if ( type == STRING) return stringValue;
		if ( type == BOOLEAN ) return booleanValue + "";
		
		return "null";
		
	}
	
}
