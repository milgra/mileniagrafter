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
   Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA. 
   
*/ 

package com.milgra.server;

/**

	Library class
	
	@mail milgra@milgra.com
	@author Milan Toth
	@version 20080315

	Tasks of Library
	
		- Hold constants

**/

public class Library 
{
	
	// io settings
	
	// PORT - port on which the server listens
	// IOTHREAD - how much threads should a process group create
	// IOBUFFER - io stream buffer size, for sockets and filechannels
	// NSBUFFER - default stream buffer size in millisecs
	// STEPTIME - process execution step
	// BANDTIME - how often the server updates bandwidth values
	// AVEXTIME - how often the server sets audio/video existence
	// PINGTIME - how often the server pings a client
	// PINGTIMEOUT - how long should the server wait for pong before disconnection
	
	public static int PORT = 1935;				
	public static int IOTHREAD = 1;			
	public static int IOBUFFER = 8096;			
	public static int NSBUFFER = 2000;
	public static int STEPTIME = 20;		
	public static int BANDTIME = 1000;		
	public static int AVEXTIME = 1000;		
	public static int PINGTIME = 15000;	
	public static int PINGTIMEOUT = 30000;	
	
	// STREAMDIR - directory for streams
	// CUSTOMDIR - directory for applications
	// CLOSEFILE - file for close triggering
	
	public static String STREAMDIR = "streams";			
	public static String CUSTOMDIR = "applications";	
	public static String CLOSEFILE = "closetrigger";	
	
	// strings
	
	public static String SALUTE = "Milenia Grafter Server 0.8b\n(c) Copyright 2007-2008 Milan Toth";						
	public static String PARAMS = "\nParameters:\nstart\nstop\nport [port]\niostep [millisecs]\niobuffer [bytes]\niothreads [threads]\napplications [directory]";
	public static String NOPORT = "Milenia not found on given port.";														
	public static String NOAPPS = "Directory '" + CUSTOMDIR + "' doesn't exist.";											
	public static String NOINST = "Invalid application: ";
	public static String OPENEX = "Socket open exception: ";
	public static String CLOSEX = "Socket close exception: ";
	public static String CODEEX = "Custom application code exception: ";
	public static String FILEEX = "File exception: ";
	public static String AMFDEX = "AMF Decode Unknown Type : ";
	
	// rtmp status strings

	public static String FAILURE = "NetConnection.Connect.Failed";
	public static String CLOSURE = "NetConnection.Connect.Closed";
	public static String SUCCESS = "NetConnection.Connect.Success";
	public static String REJECTION = "NetConnection.Connect.Rejected";

	public static String SEEK = "NetStream.Seek.Notify";
	public static String PLAYSTOP = "NetStream.Play.Stop";
	public static String PLAYSTART = "NetStream.Play.Start";
	public static String PLAYRESET = "NetStream.Play.Reset";
	public static String DATASTART = "NetStream.Data.Start";
	public static String PLAYFAILED = "NetStream.Play.Failed";
	public static String PUBLISHSTART = "NetStream.Publish.Start";
	public static String PUBLISHBADNAME = "NetStream.Publish.BadName";
	public static String RECORDNOACCESS = "NetStream.Record.NoAccess";
	public static String UNPUBLISHSUCCESS = "NetStream.Unpublish.Success";
	
	// status arrays
	
	// CONNECTKEYS - connection info object keys
	// MACPLAYERARR - connection info object values for mac player, tcUrl must contain rtmp:// for fms connection
	
	public static String HSHASH = "030e5ab20409007c021085355df292fc14575bed30f8e26966cf088fb739cce139e4155cab302a43f67f95680c7c7008030c2563fd1efa69d0ce715c0f76ab6c9902879a8d11cba69f7fa489ad8b036cd4fab44acf4a8cb03d173782db6da766e81963f70a514633e0797581074700585e15399e5c693f1501bcd10a3b8f836f63db56dda1f892332f31514419d2bf73810fdaff46bfd780b592a9260f940355aeb552d56d8073fa8783b39ed7d9ac024c3b6293492618fc1891050fbf8ee7588a7669620f1cf4cf04cfcdb41947e362f2a56df309d0c7e84b3a981e1243bab5dd7d0950f1565098030ee2327402e22e5429aead6179b58d0cdef58b04064f4fe2a4ebbd487b273142ffe1d540cea9dfece97edc18afd387e7918f43637f15a2750cbb354a44df826f3c8ab71cf50db2eb0c2cac6db7626723e1c94c446aec36ae0866cba53a861314b2dd85674b5ec18638024c55c29c5fee20ba413d1b397de50d85bd3c061e91cea43c3f9cd396f34292e4a4be0eadebd9e492c8e9a78d48ae58f11d4f88bfef89487ef446fb738278c6712797e7c6d197219cd168c19e50ff9e3f4d64137d9add36be967542d36b8a52a02a25ca6abda7f4faa7307a5d1f629eecf55546e9bc6293d1284147460725f80996ac7319052996f16428bd2245d418bb94b7b8f54b719bbc6628b0255ea782498a68b313411198f7fe812d7c13b02ab197b86857b0ad6bbace0f336563197d8c0127e9e8005d19a42210f067463a1554efb916accb413b3518fe3913f7e632d60d90a3d52847ddd74fc56b47ce3cb4ab3bbe203bf6514e1bcff03d7c4fb02f81dd5c6843ce918dc88a0e705c3411d7ab679f95773828a56b3ead4de61e1d6e0846a36c7d01572a2e94b8c6b6cf90dcbc17d13a3e27c7d7afdc0b840c457c938623d32ff32b832482051c3fcbf1c473a46ee68f7b43a038d882ab6da808e360d0ac112295f6d5c1f994d7c0af2493815869a8d4b990e3aa14a86be9e2a7ced7241a2b1833ddfc8a7ef373b71308cc2325c3568cc4ad845fc6337546543e91983d815fe145974bc46c0dfb1d8a5e444a2398fd69390a8c4d4c2b8c17bc3051c22a2f3d25fbefb9c8c554e5b79079ac5d8e51e0bfb3e8871e09808fddd439b3e7ff7578777daca08843aa1de348b43f611f74cd5ff7a5f4b87fa073fa0995fc180993c889cb622c42b3b56e24d5c41ce69056a0d1c9307c82469e7c2f85eff7fa2097437de9250a517c3b4f8423bd5ea11aef72edeec50a9064a5eecdc2c8473927a8c5620ba18e4e2e2867d3a4643a503f142d46902fc10d4e1e42ad198db5183b27c16d6caaa83ddf415b74d94972fe5adaa19f16b7435c2f4cc93b3d9eadfcaa99b5b083fb1600bf81771f48c2e4c0ff53ef8dff6dceb5e2805d3459f89945dcca6937753a0e81119a1e546f56eae7a3a321fedd9f1da2bf75bd7178152214972403f664f1bc0dab647a698d3aff512bf600749f0154eb4e5badebe0f48039056f3345915c41bb34ae6acd4045587f585e61597d2d45d3a73898d426ef53c43d4c981573805e436eb5fedc7fb34ee1df6ba7d75f9b269bbf2e5bd638674c2c9a4a25f0208b86db2bad638dd94707aa5973bc9730509abfb9205bc4decd12c29e5dd9490b1aa91e8d0efc76de00e38253cbcab74922044dc09d03eeb095c5875ff7755b3ac6bd9039ea40ca23a5b9f6e88d003e59d90b3ad58f9f0c82fb1dd9c49932759b1850966d63dc576d35afca9ecac5f10ec8d7987e811ce55e35ad1a44e3742aafdf8c454767db07d935df2cec4dd8fa38e3ebc83803582ebb79dcff001cf7652d3e804900175d3a3158ac162a0af093a2e4a87684957c69a445570e40aea8414f52c789267122694ab8f6a39fa95203455b5034a60f2557e69bc64290ed86efe074f05b9d5f4a3433601651ca6fd953573519e44493ffb52228ec588f44bf5cc48a2b94998abd88d9fc308aad7764a4f0698a467c81bac67092faced9b8e08135dc8bb021d8b5b482f99cd584d590fc3887cfe5b319518b76d0fb0b435643b6b3f4e6e9c8c194e34c2cbfb172ee5e31aaf285f53eeff62c784c073b747d44aba6654a46f823dd0e10f2a357df91389ecdc222d3c63b66e6cf590510dc5781d3aa8475e053d936768607f8";
	public static String [ ] CONNECTKEYS = { "app" , "fpad" , "tcUrl"   			 , "swfUrl" 						  , "flashVer" 					, "capabilities" , "videoCodecs"  , "audioCodecs"  , "videoFunction" , "pageUrl" 																			, "objectEncoding" };
	public static Object [ ] MACPLAYERARR = { ""   , false  , "" , "file:///Volumes/Player.swf"       , "MAC 9,0,124,0" , ( double ) 1   , ( double ) 124 , ( double ) 615 , ( double ) 1    , "file:///Volumes/Player.swf" , ( double ) 0 };
	
	// other status objects
	
	public static String [ ] STATUSKEYS = { "level" , "code" , "description" , "clientid" , "details" };
	public static Object [ ] SUCCESSARR = { "status" , SUCCESS , "Connection succeeded." , null , null };
	public static Object [ ] FAILUREARR = { "status" , FAILURE , "Connection failed." , null , null };
	public static Object [ ] CLOSUREARR = { "status" , CLOSURE , "Connection rejected." , null , null };
	public static Object [ ] REJECTIONARR = { "status" , REJECTION , "Connection rejected." , null , null };
	public static Object [ ] AMFERRORARR = { "error" , "AMF.Decode.Error" , "" , null , null };
	
	public static Object [ ] SEEKARR = { "status" , SEEK , "Seeking " , null , null } ;
	public static Object [ ] PLAYSTOPARR = { "status" , PLAYSTOP , "Stopped playing " , null , null } ;
	public static Object [ ] PLAYRESETARR = { "status" , PLAYRESET , "Playing and resetting " , null , null } ;
	public static Object [ ] PLAYSTARTARR = { "status" , PLAYSTART , "Started playing " , null , null };
	public static Object [ ] PLAYFAILEDARR = { "error" , PLAYFAILED , "Read access denied for stream " , null , null };
	public static Object [ ] PUBLISHSTARTARR = { "status" , PUBLISHSTART , " is now published." , null , null };
	public static Object [ ] PUBLISHBADNAMEARR = { "status" , PUBLISHSTART , " is now published." , null , null };
	public static Object [ ] RECORDNOACCESSARR = { "error" , RECORDNOACCESS , "Write access denied for stream " , null , null };
	public static Object [ ] UNPUBLISHSUCCESSARR = { "status" , UNPUBLISHSUCCESS , " is now unpublished." , null , null };	

}
