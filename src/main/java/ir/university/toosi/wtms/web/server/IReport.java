package ir.university.toosi.wtms.web.server;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: behzad
 * Date: 1/7/14
 * Time: 12:24 PM
 * To change this template use File | Settings | File Templates.
 */
public  abstract class IReport <T, V> {

        private JasperPrint print;
        private  InputStream inputStream;
        public JasperPrint report(List<T> list, Map<String, Object> map) throws JRException, IOException {
            try{
                inputStream=Files.newInputStream(Paths.get("/opt/report2.jasper"));
                //inputStream=EncodingFilter.class.getClassLoader().getResourceAsStream(getJasperTemplate());
            }catch (Exception e)  {
                e.printStackTrace();
            }
                     System.out.println("salam");
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);

        print = JasperFillManager.fillReport(getJasperTemplate(),
                map, dataSource);

        return print;
    }

        public void report(T object, File file) {

    }

        protected abstract InputStream getJasperTemplate() throws IOException;


}













