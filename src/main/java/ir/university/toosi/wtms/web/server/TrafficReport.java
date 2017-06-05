package ir.university.toosi.wtms.web.server;
import ir.university.toosi.tms.model.entity.TrafficLog;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created with IntelliJ IDEA.
 * User: behzad
 * Date: 1/7/14
 * Time: 12:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrafficReport extends IReport<TrafficLog,Integer> {



//	@Override
//	public Map getParameters() {
//		// TODO Auto-generated method stub
//		Map parameters = new HashMap();
//		parameters.put("", max);
//		return parameters;	}


        @Override
        public InputStream getJasperTemplate() throws IOException {
            // TODO Auto-generated method stub
           InputStream inputStream=Files.newInputStream(Paths.get("/opt/report2.jasper"));

            return inputStream;
        }





}
