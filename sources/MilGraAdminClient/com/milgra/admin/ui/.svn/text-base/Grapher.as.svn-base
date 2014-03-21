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

package com.milgra.admin.ui
{

	import com.milgra.admin.skin.GraphiconSkin;
	
	import flash.geom.Point;
	import flash.geom.Rectangle;
	
	import flash.text.TextField;
	import flash.text.TextFormat;
	import flash.text.TextFieldAutoSize;
	
	import flash.display.Sprite;
	import flash.display.Bitmap;
	import flash.display.MovieClip;
	import flash.display.BitmapData;
	
	/*
		Tasks of Grapher
			- draw up graph field
			- draw new values, shift ld values
			- update labels
	
	*/
	
	
	public class Grapher
	{
		
		public var id : String;
		public var skin : MovieClip;
		public var visible : Boolean;		
		
		// dimensions
		
		public var width : Number;
		public var height : Number;
		
		// format arrays
		
		public var texts : Array;
		public var colors : Array;
		
		// value arrays
		
		public var times : Array;
		public var values : Array;
		
		// label containers

		public var timeLabels : Array;
		public var textLabels : Array;
		public var valueLabels : Array;
		
		// info holders
		
		public var format : TextFormat;
		public var graphdata : BitmapData;
		public var markerdata : BitmapData;
		
		// info containers

		public var graph : Bitmap;
		public var marker : Bitmap;
		public var textSprite : Sprite;
		
		// helpers
		
		public var timeStepping : Number;
		public var valueStepping : Number;
		
		public var timeMarkers : Number;
		public var valueMarkers : Number;
		
		public var valueMax : Number;
		public var valueRatio : Number;
		
		public var label : TextField;
		
		/**
		 * Creates a new Grapher instance
		 **/
		
		public function Grapher ( idX : String ,
								  skinX : MovieClip , 
								  widthX : Number , 
								  heightX : Number , 
								  textsX : Array , 
								  colorsX : Array , 
								  formatX : TextFormat )
		{
			
			// trace( "Grapher.construct " + idX + " " + clipX + " " + widthX + " " + heightX + " " + textsX + " " + colorsX + " " + formatX );
			
			// init
			
			id = idX;
			skin = skinX;			
			width = widthX;
			height = heightX;
			texts = textsX;
			colors = colorsX;
			visible = true;
			valueMax = 0;
			
			times = new Array( );
			values = new Array( );
						
			textLabels = new Array( );
			timeLabels = new Array( );		
			valueLabels = new Array( );		

			format = new TextFormat( );
			
			graphdata = new BitmapData( width , 
										height , 
										true , 
										0x00000000 );
										
			markerdata = new BitmapData( width , 
										 height , 
										 false , 
										 0x000000 );

			graph = new Bitmap( graphdata );
			marker = new Bitmap( markerdata );
			textSprite = new Sprite( );
			
			// set
			
			format.font = "Arial";
			format.size = 12;
			format.color = 0x00ff00;
			
			skin.addChild( marker );
			skin.addChild( graph );
			skin.addChild( textSprite );			
			
			timeStepping = Math.floor( width / 10 );
			valueStepping = Math.floor( height / 5 );
			
			timeMarkers = Math.floor( width / timeStepping );
			valueMarkers = Math.floor( height / valueStepping );
			
			// start
			
			draw( );
			
		}
		
		/**
		 * Draws graph
		 **/
		
		public function draw ( ):void
		{
			
			//trace( "Grapher.draw" );

			markerdata.fillRect( markerdata.rect , 0x000000 );
			
			// create time label texfields, draw time markers
			
			for ( var a:int = 0 ; a < timeMarkers ; a++ )
			{
				
				label = new TextField( );
				label.autoSize = TextFieldAutoSize.LEFT;
				
				label.x = a * timeStepping;
				label.y = height - 20;
				
				label.defaultTextFormat = format;
				label.opaqueBackground = 0x000055;
				label.text = "00:00";
				
				timeLabels.push( label );
				textSprite.addChild( label );
				
				markerdata.fillRect( new Rectangle( a * timeStepping , 0 , 1 , height ) , 0x00ff00 );
				
			}
			
			// create value label textfields, draw value markers
			
			for ( var b:int = 0 ; b < valueMarkers ; b++ )
			{
				
				label = new TextField( );
				label.autoSize = TextFieldAutoSize.LEFT;
				
				label.x = 0;
				label.y = height - b * valueStepping;
				
				label.opaqueBackground = 0x000055;
				label.defaultTextFormat = format;
				label.text = "00:00";
				
				valueLabels.push( label );
				textSprite.addChild( label );
				
				markerdata.fillRect( new Rectangle( 0 , b * valueStepping , width ,1 ) , 0x22ff22 );
				
			}
			
			// create actual label textfields 
			
			for ( var c:int = 0 ; c < texts.length ; c++ )
			{
				
				label = new TextField( );
				label.autoSize = TextFieldAutoSize.RIGHT;
				
				label.defaultTextFormat = format;
				label.opaqueBackground = 0x000000;
				label.text = texts[c];
				
				textLabels.push( label );
				textSprite.addChild( label );
				
			}
			
		}
		
		/**
		 * Adds data
		 **/
		
		public function addData ( dataX:Array ):void
		{
			
			trace( id + " Grapher.addData " + dataX );
			
			var date : Date;

			// adding new values to value list
			
			values.push( dataX );
			
			// adding timestamp to time list
			
			date = new Date( );			
			times.push( date.hours + ":" + date.minutes + ":" + date.seconds );
						
			// delete unnecessary elements			
			
			if ( times.length > width )
			{
				times.splice( 0 , 1 );
				values.splice( 0 , 1 );
			}
			
			// searching for hihgest value
			
			var max:Number = 0;
			for ( var d:int = 0 ; d < dataX.length ; d++ ) if ( dataX[d] > max ) max = dataX[d];
			if ( max > valueMax ) valueMax = max * 1.2;
						
			// set time labels
			
			for ( var time : int = 0 ; time < timeLabels.length ; time++ ) 
			{
				if ( times[ time * timeStepping ] != null ) timeLabels[timeLabels.length - 1 - time ].text = times[time * timeStepping ];
				else timeLabels[timeLabels.length - 1 - time ].text = "00:00";
			}
			
			// set value labels
			
			for ( var value : int = 0 ; value < valueLabels.length ; value++ ) 
			{
				valueLabels[value ].text = Math.round( valueMax * ( height - valueLabels[value ].y ) / height );
			}
			
			// if we are not visible, don't use cpu
						
			//if ( !visible ) return;
			
			// clearing graphdata
			
			graphdata.fillRect( graphdata.rect , 0x00000000 );
			
			for ( var col : int = 0 ; col < values.length ; col++ )
			{
				
				for ( var row : int = 0 ; row < values[col ].length ; row++ )
				{
					
					var normalPosition : int = Math.round( ( values[col ][ row ] / valueMax ) * height );
					
					var xpos : int = width - values.length + col;
					var ypos : int = height - normalPosition;
					var wth : int = 1;
					var hth : int = normalPosition;
					var color : uint = colors[row ]| 0xff000000;
					
					graphdata.fillRect( new Rectangle( xpos , ypos , wth , hth ) , color );
										
					textLabels[row ].text = texts[row ] + " " + values[col ][row ];
					textLabels[row ].x = width - textLabels[row ].width;
					textLabels[row ].y = height - normalPosition - textLabels[row].height;
					
				}
				
			}
			
			// repos overlapping labels
			
			if ( textLabels.length > 1 )
			{
								
				for ( var a:int = 0 ; a < textLabels.length - 1; a++ )
				{
					for ( var b:int = 1 ; b < textLabels.length ; b++ )
					{
	
						if ( textLabels[a].y >= textLabels[b].y && 
							 textLabels[a].y <= ( textLabels[b].y + textLabels[b].height ) )
						{
							textLabels[b].y -= textLabels[a].height;
										
						}
						
					}
					
				}
			
			}

			
		}
		
		/**
		 * Makes graph visible
		 **/
		
		public function show ( ):void 
		{
			
			//trace( "Grapher.show" );
			
			visible = true;
			
			graph.visible = true;
			textSprite.visible = true;
			marker.visible = true;
				
		}
		
		/**
		 * Makes graph invisible
		 **/
		
		public function hide ( ):void 
		{
			
			//trace( "GRapher.hide" );
			
			visible = false;
			
			graph.visible = false;
			marker.visible = false;	
			textSprite.visible = false;
			
		}
		
	}
	
}