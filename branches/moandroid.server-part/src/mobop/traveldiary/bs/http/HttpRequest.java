package mobop.traveldiary.bs.http;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import android.util.Log;
/**
* HTTP Request class
*
* You can use this class and distribute it as long as you give proper credit
* and place and leave this notice intact :). Check my blog for updated
* version(s) of this class (http://moazzam-khan.com)
*
* Usage Examples:
*
* Get Request
* --------------------------------
* HttpData data = HttpRequest.get("http://example.com/index.php?user=hello");
* System.out.println(data.content);
*
* Post Request
* --------------------------------
* HttpData data = HttpRequest.post("http://xyz.com", "var1=val&var2=val2");
* System.out.println(data.content);
* Enumeration<String> keys = dat.cookies.keys(); // cookies
* while (keys.hasMoreElements()) {
* 		System.out.println(keys.nextElement() + " = " +
* 				data.cookies.get(keys.nextElement() + "rn");
*	}
* Enumeration<String> keys = dat.headers.keys(); // headers
* while (keys.hasMoreElements()) {
* 		System.out.println(keys.nextElement() + " = " +
* 				data.headers.get(keys.nextElement() + "rn");
*	}
*
* Upload a file
* --------------------------------
* ArrayList<File> files = new ArrayList();
* files.add(new File("/etc/someFile"));
* files.add(new File("/home/user/anotherFile"));
*
* Hashtable<String, String> ht = new Hashtable<String, String>();
* ht.put("var1", "val1");
*
* HttpData data = HttpRequest.post("http://xyz.com", ht, files);
* System.out.println(data.content);
*
* @author Moazzam Khan
*/
public class HttpRequest {
 
        /**
        * HttpGet request
        *
        * @param sUrl
        * @return
        */
        public static HttpData get(String sUrl) {
                HttpData ret = new HttpData();
                String str;
                StringBuffer buff = new StringBuffer();
                try {
                        URL url = new URL(sUrl);
                        URLConnection con = url.openConnection();
 
                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()), 8192);
                        while ((str = in.readLine()) != null) {
                                buff.append(str);
                        }
                        ret.content = buff.toString();
                        //get headers
                        Map<String, List<String>> headers = con.getHeaderFields();
                        Set<Entry<String, List<String>>> hKeys = headers.entrySet();
                        for (Iterator<Entry<String, List<String>>> i = hKeys.iterator(); i.hasNext();) {
                                Entry<String, List<String>> m = i.next();
 
                                Log.w("HEADER_KEY", m.getKey() + "");
                                ret.headers.put(m.getKey(), m.getValue().toString());
                                if (m.getKey().equals("set-cookie"))
                                ret.cookies.put(m.getKey(), m.getValue().toString());
                        }
                } catch (Exception e) {
                        Log.e("HttpRequest", "Get Error: " + e.toString());
                }
                return ret;
        }
 
 
 
 
        /**
        * HTTP post request
        *
        * @param sUrl
        * @param ht
        * @return
        * @throws Exception
        */
        public static HttpData post(String sUrl, Hashtable<String, String> ht) throws Exception {
                String key;
                StringBuffer data = new StringBuffer();
                Enumeration<String> keys = ht.keys();
                while (keys.hasMoreElements()) {
                        key = keys.nextElement();
                        data.append(URLEncoder.encode(key, "UTF-8"));
                        data.append("=");
                        data.append(URLEncoder.encode(ht.get(key), "UTF-8"));
                        data.append("&");
                }
                return HttpRequest.post(sUrl, data.toString());
        }
        /**
        * HTTP post request
        *
        * @param sUrl
        * @param data
        * @return
        */
        public static HttpData post(String sUrl, String data) {
                StringBuffer ret = new StringBuffer();
                HttpData dat = new HttpData();
                //String header;
                try {
                        // Send data
                        URL url = new URL(sUrl);
                        URLConnection conn = url.openConnection();
                        conn.setDoOutput(true);
                        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                        wr.write(data);
                        wr.flush();
 
                        // Get the response
 
                        Map<String, List<String>> headers = conn.getHeaderFields();
                        Set<Entry<String, List<String>>> hKeys = headers.entrySet();
                        for (Iterator<Entry<String, List<String>>> i = hKeys.iterator(); i.hasNext();) {
                                Entry<String, List<String>> m = i.next();
 
                                Log.w("HEADER_KEY", "Warning: " + m.getKey() + "");
                                dat.headers.put(m.getKey(), m.getValue().toString());
                                if (m.getKey().equals("set-cookie"))
                                dat.cookies.put(m.getKey(), m.getValue().toString());
                        }
                        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()), 8192);
                        String line;
                        while ((line = rd.readLine()) != null) {
                                ret.append(line);
                        }
                        wr.close();
                        rd.close();
                } catch (Exception e) {
                        Log.e("ERROR", "ERROR IN CODE: " + e.getMessage());
                }
                dat.content = ret.toString();
                Log.e("ERROR", "Content: " + ret.toString());
                return dat;
        }
        /**
        * Post request (upload files)
        * @param sUrl
        * @param files
        * @return HttpData
        */
        public static HttpData post(String sUrl, ArrayList<File> files)
        {
                Hashtable<String, String> ht = new Hashtable<String, String>();
                return HttpRequest.post(sUrl, ht, files);
        }
        /**
        * Post request (upload files)
        * @param sUrl
        * @param params Form data
        * @param files
        * @return
        */
        public static HttpData post(String sUrl, Hashtable<String, String> params, ArrayList<File> files) {
                HttpData ret = new HttpData();
                try {
                        String boundary = "*****************************************";
                        String newLine = "rn";
                        int bytesAvailable;
                        int bufferSize;
                        int maxBufferSize = 8192;
                        int bytesRead;
 
                        URL url = new URL(sUrl);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setDoInput(true);
                        con.setDoOutput(true);
                        con.setUseCaches(false);
                        con.setRequestMethod("POST");
                        con.setRequestProperty("Connection", "Keep-Alive");
                        con.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
                        DataOutputStream dos = new DataOutputStream(con.getOutputStream());
 
                        //dos.writeChars(params);
 
                        //upload files
                        for (int i=0; i<files.size(); i++) {
                                Log.i("HREQ", "Info: " + i + "");
                                FileInputStream fis = new FileInputStream(files.get(i));
                                dos.writeBytes("--" + boundary + newLine);
                                dos.writeBytes("Content-Disposition: form-data; "
                                + "name=\"" + "file_" + i + "\";filename=\""
                                + files.get(i).getPath() + "\"" + newLine + newLine);
                                bytesAvailable = fis.available();
                                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                byte[] buffer = new byte[bufferSize];
                                bytesRead = fis.read(buffer, 0, bufferSize);
                                while (bytesRead > 0) {
                                        dos.write(buffer, 0, bufferSize);
                                        bytesAvailable = fis.available();
                                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                        bytesRead = fis.read(buffer, 0, bufferSize);
                                }
                                dos.writeBytes(newLine);
                                dos.writeBytes("--" + boundary + "--" + newLine);
                                fis.close();
                        }
                        // Now write the data
 
                        Enumeration<String> keys = params.keys();
                        String key, val;
                        while (keys.hasMoreElements()) {
                                key = keys.nextElement().toString();
                                val = params.get(key);
                                dos.writeBytes("--" + boundary + newLine);
                                dos.writeBytes("Content-Disposition: form-data;name=\""
                                + key + "\"" + newLine + newLine + val);
                                dos.writeBytes(newLine);
                                dos.writeBytes("--" + boundary + "--" + newLine);
 
                        }
                        dos.flush();
 
                        BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()), 8192);
                        String line;
                        while ((line = rd.readLine()) != null) {
                                ret.content += line + "rn";
                        }
                        //get headers
                        Map<String, List<String>> headers = con.getHeaderFields();
                        Set<Entry<String, List<String>>> hKeys = headers.entrySet();
                        for (Iterator<Entry<String, List<String>>> i = hKeys.iterator(); i.hasNext();) {
                                Entry<String, List<String>> m = i.next();
 
                                Log.w("HEADER_KEY", "Warning: " + m.getKey() + "");
                                ret.headers.put(m.getKey(), m.getValue().toString());
                                if (m.getKey().equals("set-cookie"))
                                ret.cookies.put(m.getKey(), m.getValue().toString());
                        }
                        dos.close();
                        rd.close();
                } catch (MalformedURLException me) {
                	Log.e("HREQ", "Error: " + me.toString());
                } catch (IOException ie) {
                	Log.e("HREQ", "Error: " + ie.toString());
                } catch (Exception e) {
                	Log.e("HREQ", "Error: " + e.toString());
                }
                return ret;
        }
}
