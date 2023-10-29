package com.example.demo;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import api.ApiObjInfo;
import api.BakObjInfo;
import api.DbObjInfo;
import api.GaihiObjInfo;
import api.RemoteObjInfo;
import api.ShukeiObjInfo;
import common.utils.MyFiles;

@RestController
public class ApiController {
    @Autowired
    private SpringConfig config;

    //----------------------------------------------------------------------
	final String serv1 = "/api/kkk";
    @GetMapping(serv1)
    public String serv1Post(@RequestParam("obj") String obj) {
    //----------------------------------------------------------------------
    	ApiObjInfo objInfo = new ApiObjInfo(config, serv1, obj);
    	String msg = objInfo.makeObject();
		if (msg != null) return msg;
        
        msg = objInfo.execute();
		if (msg != null) return msg;
		
        return "OK";
    }
    
    //----------------------------------------------------------------------
	final String serv2 = "/api/hantei";
	@GetMapping(serv2)
    public String serv2Post(@RequestParam("obj") String obj) {
    //----------------------------------------------------------------------
    	ApiObjInfo objInfo = new ApiObjInfo(config, serv2, obj);
    	String msg = objInfo.makeObject();
		if (msg != null) return msg;
        
        msg = objInfo.execute();
		if (msg != null) return msg;
		
        return "OK";
    }
    
    //----------------------------------------------------------------------
	final String serv3 = "/api/kyaku";
	@GetMapping(serv3)
    public String serv3Post(@RequestParam("obj") String obj) {
    //----------------------------------------------------------------------
    	ApiObjInfo objInfo = new ApiObjInfo(config, serv3, obj);
    	String msg = objInfo.makeObject();
		if (msg != null) return msg;
        
        msg = objInfo.execute();
		if (msg != null) return msg;
		
        return "OK";
    }
    
    //----------------------------------------------------------------------
	final String serv4 = "/api/jisseki";
	@GetMapping(serv4)
    public String serv4Post(@RequestParam("obj") String obj) {
    //----------------------------------------------------------------------
    	ApiObjInfo objInfo = new ApiObjInfo(config, serv4, obj);
    	String msg = objInfo.makeObject();
		if (msg != null) return msg;
        
        msg = objInfo.execute();
		if (msg != null) return msg;
		
        return "OK";
    }
    
    //----------------------------------------------------------------------
    final String db = "/api/db";
    @GetMapping(db)
    public String dbPost(@RequestParam("obj") String obj) {
    //----------------------------------------------------------------------
        DbObjInfo objInfo = new DbObjInfo(config, db, obj);
    	String msg = objInfo.makeObject();
		if (msg != null) return msg;
        
        msg = objInfo.execute();
		if (msg != null) return msg;
		
        return "OK";
    }
    
    //----------------------------------------------------------------------
    final String shukei = "/api/shukei";
    @GetMapping(shukei)
    public String shukeiPost(@RequestParam("obj") String obj) {
    //----------------------------------------------------------------------
    	ShukeiObjInfo objInfo = new ShukeiObjInfo(config, shukei, obj);
    	String msg = objInfo.makeObject();
		if (msg != null) return msg;
        
        msg = objInfo.execute();
		if (msg != null) return msg;
		
        return "OK";
    }
    
    //----------------------------------------------------------------------
    @GetMapping(shukei+"2")
    public String shukeiGet(HttpServletResponse response, @RequestParam("obj") String obj) {
    //----------------------------------------------------------------------
    	ShukeiObjInfo objInfo = new ShukeiObjInfo(config, shukei, obj);
    	String msg = objInfo.makeObject();
		if (msg != null) return msg;
        
		String[] filePath = new String[1]; 
        msg = objInfo.download(filePath);
		if (msg != null) return msg;
		
        return filedownload(response, filePath[0]);
    }
    
    //----------------------------------------------------------------------
    final String backup = "/api/backup";
    @PostMapping(backup)
    public String backupPost(@RequestParam("obj") String obj) {
    //----------------------------------------------------------------------
    	BakObjInfo objInfo = new BakObjInfo(config, backup, obj);
    	String msg = objInfo.makeObject();
		if (msg != null) return msg;
        
        msg = objInfo.execute();
		if (msg != null) return msg;
		
    	return "OK";
    }
    
    //----------------------------------------------------------------------
    final String gaihi = "/api/gaihi";
    @PostMapping(gaihi)
    //public String gaihiPost(@RequestParam("obj") String obj) {
    public String gaihiPost() {
    //----------------------------------------------------------------------
    	GaihiObjInfo objInfo = new GaihiObjInfo(config, gaihi, null);
    	String msg = objInfo.makeObject();
		if (msg != null) return msg;
        
        msg = objInfo.execute();
		if (msg != null) return msg;
		
    	return "OK";
    }
    
    //----------------------------------------------------------------------
    final String remote = "/api/remote";
    @PostMapping(remote)
    public String remotePost(@RequestParam("obj") String obj) {
    //----------------------------------------------------------------------
    	RemoteObjInfo objInfo = new RemoteObjInfo(config, remote, obj);
    	String msg = objInfo.makeObject();
		if (msg != null) return msg;
        
        msg = objInfo.execute();
		if (msg != null) return msg;
		
    	return "OK";
    }
    
    //テストファイルダウンロード
    //curl -X GET "http://localhost:8080/daicho"
    /* @GetMapping("daicho")
    public void apiDaicho(HttpServletResponse response){
		ArrayList<OcrDaichoBean> list;
		try {
	        list = OcrRirekiDAO.getInstance(config).getDaicho();
	        
			response.setContentType("text/tsv;charset=UTF8");
			String fileName = new String("daicho.tsv".getBytes("UTF-8"), "ISO-8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			PrintWriter writer = response.getWriter();
  			String line;
			//ヘッダ
			line = "ID\t日時\tOCR\tNo\t取引先\t日付\t送付先\t注番\t品名\t数量\t単価\t金額\t備考\r\n";
			writer.write(line);
			//データ
			for (int i=0; i<list.size(); i++) {
				OcrDaichoBean dto = (OcrDaichoBean)list.get(i);
	  			line = dto.getAllData();
	  			writer.write(line);
			}
			writer.close();
		} catch (IOException e) {
			list = null;
			e.printStackTrace();
		}	
    }*/    
    
    private String filedownload(HttpServletResponse response, String filePath) {
    	
        try (OutputStream os = response.getOutputStream();) {
        	String fileName = MyFiles.getFileName(filePath);
            byte[] fb = MyFiles.readAllBytes(filePath);
            
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setContentLength(fb.length);
            os.write(fb);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        	return e.toString();
        }
		
        return "OK";
    }    
}
