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

package com.milgra.admin
{

	import com.milgra.admin.CustomEvent;	
	import com.milgra.admin.ui.Grapher;
	import com.milgra.admin.ui.SimpleButton;
	import com.milgra.admin.skin.GraphiconSkin;
	
	/*
		Tasks of Graphicondata
			- initialize graphers and switcher buttons
			- update graphers
	
	*/
	
	
	public class GraphiconData
	{
		
		public var skin : GraphiconSkin;
		
		public var bandsGraph : Grapher;
		public var clientsGraph : Grapher;
		public var streamsGraph : Grapher;
		public var timesGraph : Grapher;
		public var threadsGraph : Grapher;
		
		public var bandsButton : SimpleButton;
		public var clientsButton : SimpleButton;
		public var streamsButton : SimpleButton;
		public var timesButton : SimpleButton;
		public var threadsButton : SimpleButton;
		
		/**
		 * Creates a new GraphController instance
		 * @param skinX skin
		 **/
				
		public function GraphiconData ( skinX : GraphiconSkin )
		{
			
			// trace( "GraphController.construct " + skinX );
			
			// create

			bandsButton = new SimpleButton( skinX.buttonSkin0 );
			clientsButton = new SimpleButton( skinX.buttonSkin2 );
			streamsButton = new SimpleButton( skinX.buttonSkin1 );
			timesButton = new SimpleButton( skinX.buttonSkin3 );
			threadsButton = new SimpleButton( skinX.buttonSkin4 );
			
			bandsGraph = new Grapher( "bands" , 
									  skinX.displaySkin0 , 
									  690 , 
									  260 , 
									  [ "overall Mbit/sec" , "incoming Mbit/sec:" , "outgoing Mbytes/sec:" ] , 
									  [ 0xaaaa00 , 0x00ff00 , 0x00ffff ] , 
									  null );
									  
			timesGraph = new Grapher( "times" , 
									  skinX.displaySkin0 , 
									  690 , 
									  260 ,
									  ["socket processing time:" , "stream processing time:" , "client processing time:" ], 
									  [0xaaaa00 , 0x00ff00 , 0x00ffff ] , 
									  null );
									  
			clientsGraph = new Grapher( "clients" , 
										skinX.displaySkin0 , 
										690 , 
										260 , 
										["clients" ] ,
										[0xffff00 ] , 
										null );
										
			streamsGraph = new Grapher( "streams" , 
										skinX.displaySkin0 , 
										690 , 
										260 , 
										["overall" , "incoming" , "outgoing" ] ,
										[0xffff00 , 0x00ffff , 0x00ff00 ] , 
										null );
										
			threadsGraph = new Grapher( "threads" , 
										skinX.displaySkin0 , 
										690 , 
										260 , 
										["socketpool threads:" , "streampool threads:" , "clientpool threads:" ],
										[0xaaaa00 , 0x00ff00 , 0x00ffff ], 
										null );
										
			// set
			
			skin = skinX;
			
			// event
									
			bandsButton.addEventListener( CustomEvent.ACTIVATE , onBands );
			clientsButton.addEventListener( CustomEvent.ACTIVATE , onClients );
			streamsButton.addEventListener( CustomEvent.ACTIVATE , onStreams );
			timesButton.addEventListener( CustomEvent.ACTIVATE , onTimes );
			threadsButton.addEventListener( CustomEvent.ACTIVATE , onThreads );
			
			// start
			
			bandsGraph.show( );
			timesGraph.hide( );
			clientsGraph.hide( );
			streamsGraph.hide( );
			threadsGraph.hide( );
			
		}
		
		/**
		 * Button events
		 **/
		
		public function onBands ( eventX : CustomEvent ):void { onSwitch( "bands" ); }
		public function onTimes ( eventX : CustomEvent ):void { onSwitch( "times" ); }
		public function onClients ( eventX : CustomEvent ):void { onSwitch( "clients" ); }
		public function onThreads ( eventX : CustomEvent ):void { onSwitch( "threads" ); }
		public function onStreams ( eventX : CustomEvent ):void { onSwitch( "streams" ); }
		
		/**
		 * Switch
		 * @param stateX new state
		 **/
		
		public function onSwitch ( stateX : String ):void
		{
			
			// trace( "GraphController.onSwitch " + stateX );
			
			if ( stateX == "bands" ) bandsGraph.show( ); else bandsGraph.hide( );
			if ( stateX == "times" ) timesGraph.show( ); else timesGraph.hide( );
			if ( stateX == "clients" ) clientsGraph.show( ); else clientsGraph.hide( );
			if ( stateX == "streams" ) streamsGraph.show( ); else streamsGraph.hide( );
			if ( stateX == "threads" ) threadsGraph.show( ); else threadsGraph.hide( );
			
		}
		
		/**
		 * Updates graphs
		 * @param dataX data
		 **/
		
		public function update ( dataX : Object ):void
		{
			
			// trace( "GraphController.update " + dataX );
			
			bandsGraph.addData( [ dataX.bandin + dataX.bandout , dataX.bandin , dataX.bandout ]  );
			timesGraph.addData( [ dataX.sockettime , dataX.streamtime , dataX.clienttime ] );
			clientsGraph.addData( [ dataX.clients ] );
			streamsGraph.addData( [ dataX.streamsin + dataX.streamsout , dataX.streamsin , dataX.streamsout ]);
			threadsGraph.addData( [ dataX.socketcount , dataX.streamcount , dataX.clientcount ] );
			
		}
		
	}
	
}