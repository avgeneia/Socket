package com.mhms;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MhmsApplication{
	
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(MhmsApplication.class, args);
		
		InetAddress local;
		try {
			local = InetAddress.getLocalHost();
			String ip = local.getHostAddress();
			
			String lastIp = ip.split(".")[3];
			System.out.println("lastIp :::::::::::::::::::::::: " + lastIp);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		Connection connection = null;
//		PreparedStatement prep = null;
//		ResultSet row = null;
//		
//		try {
//			connection = DriverManager.getConnection("jdbc:sqlite:/D:/workspace/MHMS/mydb.db");
//			prep = connection.prepareStatement("select * from UserLogin;");
//			
//			row = prep.executeQuery();
//		} catch(Exception e) {
//			System.out.println(e);
//		}
//		

	}
		
//	@GetMapping("/hello")
//	public String hello(@RequestParam(value = "name", defaultValue = "World23234") String name) {
//		return String.format("Hello %s!", name);
//	}
//	
//	@GetMapping("/test")
//    public ModelAndView getUser(Model model) {
//		ModelAndView modelAndView = new ModelAndView();
//        
//        modelAndView.setViewName("test");
//		
//        
//        return modelAndView;
//    }
//	
//	@GetMapping(value = "/")
//    public ModelAndView home() {
//        ModelAndView modelAndView = new ModelAndView();
//        
//        modelAndView.setViewName("login");
//        
//        Map<String, Object> map = new HashMap<>();
//        map.put("name", "Bamdule");
//        map.put("date", LocalDateTime.now());
//        
//        modelAndView.addObject("data", map);
//        
//        return modelAndView;
//    }
//	
//	@GetMapping("/boot")
//    public ModelAndView bootstrap(Model model) {
//		ModelAndView modelAndView = new ModelAndView();
//        
//        modelAndView.setViewName("boot");
//		
//        
//        return modelAndView;
//    }

}