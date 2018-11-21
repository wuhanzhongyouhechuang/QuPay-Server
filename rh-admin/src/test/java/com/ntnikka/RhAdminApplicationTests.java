package com.ntnikka;

import com.github.crab2died.ExcelUtils;
import com.ntnikka.model.GbTempEntity;
import com.ntnikka.modules.apply.entity.GbEntity;
import com.ntnikka.modules.apply.entity.MyGbEntity;
import com.ntnikka.modules.apply.service.GbService;
import com.ntnikka.modules.apply.service.MyGbService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class RhAdminApplicationTests {

    @Autowired
    private GbService gbService;

    @Autowired
    private MyGbService myGbService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void excel2Object() throws Exception {

//		String path = "C:\\home\\最新2017年国民经济行业分类(GB-T-4754—2017).xlsx";
//
//		System.out.println("读取全部：");
//		List<GbTempEntity> gbcodes = ExcelUtils.getInstance().readExcel2Objects(path, GbTempEntity.class);
//		for (GbTempEntity gbtp : gbcodes) {
//			GbEntity gb=new GbEntity();
//			gb.setCid(gbtp.getCid());
//			gb.setName(gbtp.getName());
//			gb.setBid(gbtp.getBid());
//			gb.setBname(gbtp.getBname());
//			gb.setMid(gbtp.getMid());
//			gb.setMname(gbtp.getMname());
//			gb.setLid(gbtp.getLid());
//			gb.setLname(gbtp.getLname());
//			gbService.save(gb);
//			System.out.println(gb.getBname());
//		}
//		System.out.println("读取指定行数：");
//		gbcodes = ExcelUtils.getInstance().readExcel2Objects(path, GbTempEntity.class, 0, 3, 0);
//		for (GbTempEntity gb : gbcodes) {
//			System.out.println(gb);
//		}

        //数据库转换 gb_lib => gb_mylib
        //存一级大类 A,B,C ...
//		List<Map<String,Object>> rt = gbService.getAllCids();
//		for (int i = 0; i < rt.size(); i++) {
//			Map<String, Object> map = rt.get(i);
//			System.out.println(map.get("cid")+"->"+map.get("name"));
//			MyGbEntity mygb = new MyGbEntity();
//			mygb.setPid(1L);
//			mygb.setCode((String) map.get("cid"));
//			mygb.setName((String) map.get("name"));
//			myGbService.save(mygb);
//		}

        //存二级类 01,02,03 ...

//		List<Map<String,Object>> rt = gbService.getAllBids();
//		for (int i = 0; i < rt.size() ; i++) {
//			Map<String, Object> map = rt.get(i);
////			System.out.println(myGbService.getIdByCode((String) map.get("cid"))+"->"+map.get("bid")+">"+map.get("bname"));
//			MyGbEntity mygb = new MyGbEntity();
//			mygb.setPid(myGbService.getIdByCode((String) map.get("cid")));
//			mygb.setCode((String) map.get("bid"));
//			mygb.setName((String) map.get("bname"));
//			myGbService.save(mygb);
//		}

//		//存三级类 011,012,,013 ...
//		List<Map<String,Object>> rt = gbService.getAllMids();
//		for (int i = 0; i < rt.size() ; i++) {
//			Map<String, Object> map = rt.get(i);
////			System.out.println(myGbService.getIdByCode((String) map.get("bid"))+"->"+map.get("mid")+">"+map.get("mname"));
//			MyGbEntity mygb = new MyGbEntity();
//			mygb.setPid(myGbService.getIdByCode((String) map.get("bid")));
//			mygb.setCode((String) map.get("mid"));
//			mygb.setName((String) map.get("mname"));
//			myGbService.save(mygb);
//		}

        //存四级类 0111,0112,,0113 ...
//		List<Map<String,Object>> rt = gbService.getAllLids();
//		for (int i = 0; i < rt.size() ; i++) {
//			Map<String, Object> map = rt.get(i);
////			System.out.println(myGbService.getIdByCode((String) map.get("mid"))+"->"+map.get("lid")+">"+map.get("lname"));
//			MyGbEntity mygb = new MyGbEntity();
//			mygb.setPid(myGbService.getIdByCode((String) map.get("mid")));
//			mygb.setCode((String) map.get("lid"));
//			mygb.setName((String) map.get("lname"));
//			myGbService.save(mygb);
//		}

    }

}
