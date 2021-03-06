package com.telenav.cserver.poi.struts.action.v20;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;

import junit.framework.TestCase;

import com.telenav.cserver.service.servlet.TelenavServiceServlet;
import com.telenav.cserver.util.MockHttpServletRequest;
import com.telenav.cserver.util.MockHttpServletResponse;
import com.telenav.j2me.datatypes.DataConstants;
import com.telenav.j2me.datatypes.TxNode;

public class TestPOIAction_CS extends TestCase {
	private MockHttpServletRequest httpRequest;
	private MockHttpServletResponse httpResponse;

	@Override
	protected void setUp() throws Exception {
		TxNode profileNode=new TxNode();
		TxNode userInfoNode=new TxNode();
		userInfoNode.addValue(DataConstants.USER_INFO);
		userInfoNode.addMsg("5555215554");
		userInfoNode.addMsg("2096");
		userInfoNode.addMsg("9945862");
		userInfoNode.addMsg("");
		userInfoNode.addMsg("en_IN");
		userInfoNode.addMsg("IN");
		userInfoNode.addMsg("");
		userInfoNode.addMsg("");
		TxNode clientVersionNode=new TxNode();
		clientVersionNode.addValue(DataConstants.CLIENT_VERSION);
		clientVersionNode.addMsg("MMI");
		clientVersionNode.addMsg("RIM");
		clientVersionNode.addMsg("6.4.01");
		clientVersionNode.addMsg("generic480X360");
		clientVersionNode.addMsg("6201015");
		clientVersionNode.addMsg("");
		clientVersionNode.addMsg("TN");
		clientVersionNode.addMsg("");
		TxNode userPrefsNode=new TxNode();
		userPrefsNode.addValue(DataConstants.USER_PREFS);
		userPrefsNode.addMsg("mp3hi");
		userPrefsNode.addMsg("");
		userPrefsNode.addMsg("");
		userPrefsNode.addMsg("480-360");
		userPrefsNode.addMsg("360-480");
		profileNode.addMsg("profile");
		profileNode.addChild(userPrefsNode);
		profileNode.addChild(userInfoNode);
		profileNode.addChild(clientVersionNode);
		
		TxNode gpsNode=new TxNode();
		gpsNode.addMsg("gps");
		
		TxNode executorNode=new TxNode();
		executorNode.addMsg("SearchPoi20");
		executorNode.addMsg("");
		executorNode.addValue(595);
		executorNode.addValue(5);	
		executorNode.addValue(1);	
		executorNode.addValue(3);	
		executorNode.addValue(0);	
		executorNode.addValue(9);	
		executorNode.addValue(0);	
		executorNode.addValue(0);	
		executorNode.addValue(0);		
		TxNode stopNode=new TxNode();
		stopNode.addValue(DataConstants.TYPE_STOP);
		stopNode.addValue(4367160);
		stopNode.addValue(-7937999);
		stopNode.addValue(6);
		executorNode.addChild(stopNode);
				
		TxNode rootNode=new TxNode();
		rootNode.addChild(profileNode);
		rootNode.addChild(gpsNode);
		rootNode.addChild(executorNode);
		
		httpRequest=new MockHttpServletRequest(TxNode.toByteArray(rootNode));
		httpResponse=new MockHttpServletResponse();
	}

	public void testExecute() throws Exception {
		TelenavServiceServlet servlet=new TelenavServiceServlet();
		Class<TelenavServiceServlet> cls=TelenavServiceServlet.class;
		Method mtd=cls.getSuperclass().getDeclaredMethod("doPost", new Class[]{HttpServletRequest.class, HttpServletResponse.class});
		mtd.setAccessible(true);
		mtd.invoke(servlet, new Object[]{httpRequest,httpResponse});		
		byte[] bytes = httpResponse.getOutputStreamBytes();
		TxNode node = TxNode.fromByteArray(bytes, 0);
		Assert.assertNotNull(node);
		System.out.println(node);
		
	}
}
