package ir.university.toosi.tms.util;


import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */
public class FileUtils {

    private static final String CT_IMAGE_GIF = "image/gif";
    private static final String CT_IMAGE_JPEG = "image/jpeg";
    private static final String CT_IMAGE_TIFF = "image/tiff";
    private static final String CT_IMAGE_PNG = "image/png";
    private static final String CT_IMAGE_WBMP = "image/wbmp";
    private static final String CT_IMAGE_BMP = "image/bmp";
    private String basename;
    private List<String> files = new ArrayList<>();
    private List<String> jpdls = new ArrayList<>();


    // Unreserved punctuation mark/symbols
    private static String mark = "-_.!~*'()\"";

    public static void uploadFile(String fileName, byte[] content) throws IOException {


        if (fileName == null || fileName.length() < 1 ||
                content == null || content.length == 0) {
            throw new IOException("How create a null file");
        }

        String uri = new StringBuffer(Configuration.getProperty("jboss.name"))
                .append(File.separator)
                .append("server")
                .append(File.separator)
                .append(Configuration.getProperty("jboss.server"))
                .append(File.separator)
                .append(Configuration.getProperty("global.dir"))
                .append(File.separator)
                .append(Configuration.getProperty("global.dir") + ".war")
                .append(File.separator)
                .append(fileName).toString();

        FileOutputStream fout = new FileOutputStream(uri);
        fout.write(content);
        fout.flush();
        fout.close();

    }


    public static void download(String address, String localFileName) {

        OutputStream out = null;
        URLConnection conn = null;
        InputStream in = null;
        try {
            URL url = new URL(address);
            out = new BufferedOutputStream(
                    new FileOutputStream(localFileName));
            conn = url.openConnection();
            in = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int numRead;
            long numWritten = 0;
            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
                numWritten += numRead;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ioe) {
            }
        }
    }

    public static void download(String address) {
        int lastSlashIndex = address.lastIndexOf('/');
        if (lastSlashIndex >= 0 &&
                lastSlashIndex < address.length() - 1) {
            download(address, address.substring(lastSlashIndex + 1));
        } else {
            System.err.println("Could not figure out local file name for " +
                    address);
        }
    }


    public static byte[] stringToByte(String content) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(content.getBytes("UTF-8"));
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static String readFile(String filePath) {
        StringBuffer content = new StringBuffer();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                content.append(line + '\n');
            }
            in.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return content.toString();
    }

    /**
     * Converts Hex digit to a UTF-8 "Hex" character
     *
     * @param digitValue digit to convert to Hex
     * @return the converted Hex digit
     */
    static private char toHexChar(int digitValue) {
        if (digitValue < 10)
            // Convert value 0-9 to char 0-9 hex char
            return (char) ('0' + digitValue);
        else
            // Convert value 10-15 to A-F hex char
            return (char) ('A' + (digitValue - 10));
    }

    /**
     * Encodes a URL - This method assumes UTF-8
     *
     * @param url URL to encode
     * @return the encoded URL
     *         From: http://www.cenriqueortiz.com/techtips/ttip_EncodeUrl.html
     */
    static public String encodeURL(String url) {
        StringBuffer encodedUrl = new StringBuffer(); // Encoded URL
        int len = url.length();
// Encode each URL character
        for (int i = 0; i < len; i++) {
            char c = url.charAt(i); // Get next character
            if ((c >= '0' && c <= '9') ||
                    (c >= 'a' && c <= 'z') ||
                    (c >= 'A' && c <= 'Z'))
                // Alphanumeric characters require no encoding, append as is
                encodedUrl.append(c);
            else {
                int imark = mark.indexOf(c);
                if (imark >= 0) {
                    // Unreserved punctuation marks and symbols require
                    //  no encoding, append as is
                    encodedUrl.append(c);
                } else {
                    // Encode all other characters to Hex, using the format "%XX",
                    //  where XX are the hex digits
                    encodedUrl.append('%'); // Add % character
// Encode the character's high-order nibble to Hex
                    encodedUrl.append(toHexChar((c & 0xF0) >> 4));
// Encode the character's low-order nibble to Hex
                    encodedUrl.append(toHexChar(c & 0x0F));
                }
            }
        }
        return encodedUrl.toString(); // Return encoded URL
    }

    /**
     * Eimaa days :)
     *
     * @param ext
     * @return mime type of extention
     */
    public static String guessContentType(String ext) {
        String extention = ext.substring(ext.lastIndexOf('.') + 1);

        if (extention.equalsIgnoreCase("gif"))
            return CT_IMAGE_GIF;
        else if (extention.equalsIgnoreCase("jpg") || extention.equalsIgnoreCase("jpeg"))
            return CT_IMAGE_JPEG;
        else if (extention.equalsIgnoreCase("png"))
            return CT_IMAGE_PNG;
        else if (extention.equalsIgnoreCase("tiff"))
            return CT_IMAGE_TIFF;
        else if (extention.equalsIgnoreCase("wbmp"))
            return CT_IMAGE_WBMP;
        else if (extention.equalsIgnoreCase("bmp"))
            return CT_IMAGE_BMP;
        return "";
    }

    public List<String> getJpdls(File dir) {
        File[] list = dir.listFiles();
        for (File f : list) {
            if (f.isDirectory()) {
                getPages(f);
            } else if (f.isFile()) {
                if (f.getName().endsWith(".xml")) {
                    String realName = f.getName();
                    String temp = realName.substring(0, realName.indexOf("."));
                    jpdls.add(temp);
                }
            }
        }
        return jpdls;
    }

    public List<String> getPages(File dir) {
        File[] list = dir.listFiles();
        for (File f : list) {
            if (f.isDirectory()) {
                getPages(f);
            } else if (f.isFile()) {
                if (f.getName().endsWith(".xhtml")) {
                    String realPath = f.getPath();
                    String temp = realPath.substring(realPath.indexOf("view/")).substring(4);
                    files.add(temp);
                }
            }
        }
        return files;
    }

    public String[] getActionClass(String path) {
        FilenameFilter filter = new JavaFilter();
        return new File(path).list(filter);
    }

    class JavaFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".java"));
        }
    }

    class XhtmlFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".xhtml"));
        }
    }

    private void copyDirectory(File sourcePath, File destinationPath) throws IOException {
        if (sourcePath.isDirectory()) {
            if (!destinationPath.exists()) {
                destinationPath.mkdir();
            }

            String files[] = sourcePath.list();

            for (int counter = 0; counter < files.length; counter++) {
                copyDirectory(new File(sourcePath, files[counter]), new File(destinationPath, files[counter]));
            }
        } else {
            if (sourcePath.exists()) {
                InputStream in = new FileInputStream(sourcePath);
                OutputStream out = new FileOutputStream(destinationPath);

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }
        }
    }


    public void createProject(String projectName, String projectPath) {
        File sourseFile = new File(Configuration.getProperty("jboss.name") + File.separator + "server" + File.separator + Configuration.getProperty("jboss.server") + File.separator + Configuration.getProperty("global.dir") + File.separator + "InstanceObject");
        File destinationFile = null;

        if (String.valueOf(projectPath.charAt(projectPath.length() - 1)).equalsIgnoreCase(File.separator)) {
            destinationFile = new File(projectPath + projectName);
        } else {
            destinationFile = new File(projectPath + File.separator + projectName);
        }

        try {
            copyDirectory(sourseFile, destinationFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void replaceFileContent(String filename, String oldValue, String newValue, boolean isProperty) {
        try {
            File file = new File(filename);

            String content = "";

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (isProperty) {
                    content += line + '\n';
                } else {
                    content += line;
                }
            }

            bufferedReader.close();

            content = content.replaceAll(oldValue, newValue);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            outputStreamWriter.write(content);
            outputStreamWriter.flush();
            outputStreamWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setProperty(String filename, String key, String newValue) {
        Properties props = new Properties();

        try {
            props.load(new FileInputStream(filename));
            props.setProperty(key, newValue);
            props.store(new FileOutputStream(filename), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}