/*

	Milenia Grafter Server
	
	Copyright (c) 2007-2008 by Milan Toth. All rights reserved.
	
	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

*/

package com.milgra.unit.modules
{
		
	import com.milgra.unit.CustomEvent;
	import com.milgra.unit.RTMPBandwidth;
	import com.milgra.unit.RTMPConnection;
	
	import com.milgra.unit.ui.Textbox;
	import com.milgra.unit.ui.Scrollbar;
	import com.milgra.unit.skin.TextSkin;
	
	import flash.text.TextFormat;
	
	/*
		Tasks of CSBandwidth
			- test bandwidth
			- test badwidth testing :)
	
	*/
	
	public class CSBandwidth
	{
				
		// skin - module skin
		
		public var skin : TextSkin;
		
		// hostUrl - url of host application
		// helpUrl - url of helper application
		
		public var hostUrl : String;
		public var helpUrl : String;
		
		// rtmp - rtmp handler
		// checker - bandwidth checker class
		
		public var rtmp : RTMPConnection;
		public var checker : RTMPBandwidth;
		
		// output - textbox
		// scroll - scroller for textbox
		// format - textformat for box
		
		public var output : Textbox;
		public var scroll : Scrollbar;
		public var format : TextFormat;
		
		/**
		 * CSConnection construct
		 * @param skinX skin
		 * @param hostX host url
		 * @param helpX helper url
		 **/
		
		public function CSBandwidth ( skinX : TextSkin ,
									  hostX : String , 
									  helpX : String )
		{

			// trace( "CSBandwidth.construct " + skinX + " " + hostUrl + " " + helpUrl );
			
			// create
			
			rtmp = new RTMPConnection( );
			checker = new RTMPBandwidth( rtmp.connection );
			
			format = new TextFormat( "Verdana" , 12 , 0 );
			scroll = new Scrollbar( skinX.scrollbarSkin0 ); 	
			output = new Textbox( skinX.field0 , format );
			
			// set
			
			hostUrl = hostX;
			helpUrl = helpX;
			
			skin = skinX;
						
			// event

			rtmp.addEventListener( CustomEvent.ACTIVATE , onActivate );
			rtmp.addEventListener( CustomEvent.DEACTIVATE , onDeactivate );
			
			// start			
			
			output.clear( );
			output.add( "Connecting..." );
			rtmp.connection.connect( "rtmp://" + hostX + "/milgraunit" , "CSBandwidth" );
			
		}
		
		/**
		 * Close module
		 **/
		
		public function close ( ) : void
		{
			
			trace( "CSBandwidth.close" );
			rtmp.connection.close( );
			
		}
		
		/**
		 * Connection established
		 * @param eventX event
		 **/
		
		public function onActivate ( eventX : CustomEvent ) : void
		{
			
			trace( "CSBandwidth.onActivate " + eventX );
			
			uploadCheck( );
			
		}
		
		/**
		 * Connection failed
		 * @param eventX event
		 **/
		
		public function onDeactivate ( eventX : CustomEvent ) : void
		{
		
			trace( "CSBandwidth.onDeactivate " + eventX );	
			output.add( "Connection failed." );
			
		}
	
		/**
		 * Checking upload bandwidth
		 **/
		
		public function uploadCheck ( ) : void
		{
			
			// trace( "CSBandwidth.uploadCheck" );
			output.add( "Checking download bandwidth..." );

			checker.addEventListener( RTMPBandwidth.EVENT_PROGRESS , onProgress );
			checker.addEventListener( RTMPBandwidth.EVENT_COMPLETE , onUploadCheck );
			checker.uploadCheck( );			
			
		}
		
		/**
		 * Upload check result
		 * @param eventX event
		 **/
		
		public function onUploadCheck ( eventX : CustomEvent ) : void
		{
			
			// trace( "CSBandwidth.onUploadCheck " + eventX.bandwidth );
			output.add( "Upload bandwidth: " + eventX.bandwidth + " bytes/sec" );
			
			checker.removeEventListener( RTMPBandwidth.EVENT_PROGRESS , onProgress );
			checker.removeEventListener( RTMPBandwidth.EVENT_COMPLETE , onUploadCheck );

			checker.addEventListener( RTMPBandwidth.EVENT_PROGRESS , onProgress );
			checker.addEventListener( RTMPBandwidth.EVENT_COMPLETE , onDownloadCheck );
			
			checker.downloadCheck( );
			
		}
		
		/**
		 * Download check result
		 * @param eventX event
		 **/
		
		public function onDownloadCheck ( eventX : CustomEvent ) : void
		{
		
			// trace( "CSBandwidth.onDownloadCheck " + eventX );	
			output.add( "Download bandwidth: " + eventX.bandwidth + " bytes/sec" );
			
			checker.removeEventListener( RTMPBandwidth.EVENT_COMPLETE , onDownloadCheck );
			output.add( "Finished testing." );
			
		}
		
		/**
		 * Checking progress
		 * @param eventX event
		 **/
		
		public function onProgress ( eventX : CustomEvent ) : void
		{
			
			// trace( "CSBAndwidth.onProgress " + eventX );
			
			output.add( "Progress: " + eventX.ratio * 100 );
			
		}

	}
	
}