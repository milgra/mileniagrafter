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
	
	WrapperList class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080316

	Tasks of WrapperList
	
		- contain multiple wrappers as a list
 
**/

import java.util.List;
import java.util.ArrayList;

public class WrapperList extends ArrayList < Wrapper > 
{
	
	public static final long serialVersionUID = 0;
	
	public WrapperList ( )	{ super( );	}
	public WrapperList ( Wrapper itemX ) { super( ); add( itemX ); }
	public WrapperList ( List < Wrapper > sourceX ) { super( sourceX );	}
	
	public void add ( )						{ add( new Wrapper( ) ); }
	public void add ( String stringX 	)	{ add( new Wrapper( stringX ) ); 	}	
	public void add ( double doubleX 	)	{ add( new Wrapper( doubleX ) );	}
	public void add ( boolean booleanX 	) 	{ add( new Wrapper( booleanX ) );	}
	public void add ( WrapperMap mapX 	)	{ add( new Wrapper( mapX ) ); 		}
	public void add ( WrapperList listX ) 	{ add( new Wrapper( listX ) ); 		}
	
	public String getType 		( int indexX )	{ return get( indexX ).type; 		}
	public String getString 	( int indexX )	{ return get( indexX ).stringValue; }
	public double getDouble 	( int indexX )	{ return get( indexX ).doubleValue; }
	public boolean getBoolean 	( int indexX ) 	{ return get( indexX ).booleanValue;}
	public WrapperMap getMap 	( int indexX ) 	{ return get( indexX ).mapValue;	}
	public WrapperList getList 	( int indexX ) 	{ return get( indexX ).listValue; 	}
	
}
