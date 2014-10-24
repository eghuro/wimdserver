/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wimdserver.db.sync.drivers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import wimdserver.db.sync.model.AuthSalt;
import wimdserver.db.sync.model.AuthSession;
import wimdserver.db.sync.model.DRDBDevice;
import wimdserver.db.sync.model.DRDBRecord;
import wimdserver.db.sync.model.Row;
import wimdserver.db.sync.model.UserDevice;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class XMLDriver implements IDriver {
    final String OUT_ENC="UTF-8";
    final Document DOC;
    final File F;
    
    public XMLDriver(String fname) throws IOException, SAXException, ParserConfigurationException{
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        this.F = new File(fname);
        if(F.exists()){
            DOC = db.parse(new File(fname));
        }else{
            DOC = db.newDocument();
        }
        
    }
    
    /*
        <doc>
            <table name="">
                <row primary="">
                    <column name="" value="" />
                </row>
            </table>
       </doc>
    */

    @Override
    public Row getRowByKey(String table, String s) throws ParseException{
        if((table==null)||(s==null))
            return null;
        NodeList tables = DOC.getElementsByTagName("table");
        boolean found=false;
        Element n=null;
        for(int i=0;i<tables.getLength();i++){
            n = (Element)tables.item(i);
            if(n.getAttribute("name").equals(table)){
                found=true;
                break;
            }
        }
        if(found&&(n!=null)){
            NodeList rows = n.getChildNodes();
            Element r=null;
            boolean f=false;
            for(int i=0;i<rows.getLength();i++){
                r=(Element)rows.item(i);
                if(r.getAttribute("primary").equals(s)){
                    f=true;
                    break;
                }
            }
            if(f&&(r!=null)){
                NodeList columns = r.getChildNodes();        
                switch(table){
                    case "AuthSalt":
                        String hash=null,salt=null;
                        for(int i=0;i<columns.getLength();i++){
                            Element c = (Element)columns.item(i);
                            switch(c.getAttribute("name")){
                                case "hash":
                                    hash=c.getAttribute("value");
                                    break;
                                case "salt":
                                    salt=c.getAttribute("value");
                                    break;
                                default:
                                    throw new ParseException(c.getAttribute("name"),0);
                            }
                        }
                        if((hash!=null)&&(salt!=null)){
                            return new AuthSalt(s,hash,salt);
                        }
                        break;
                    case "AuthSession":
                        String uid=null,validity=null;
                        for(int i=0;i<columns.getLength();i++){
                            Element c = (Element)columns.item(i);
                            switch(c.getAttribute("name")){
                                case "uid":
                                    uid=c.getAttribute("value");
                                    break;
                                case "validity":
                                    validity = c.getAttribute("value");
                                    break;
                                default:
                                    throw new ParseException(c.getAttribute("name"),0);
                            }
                        }
                        if((uid!=null)&&(validity!=null)){
                            return new AuthSession(s,uid,AuthSession.getDateFormat().parse(validity));
                        }
                        break;
                    case "DRDBDevice":
                        String otp=null,salt_=null;
                        for(int i=0;i<columns.getLength();i++){
                            Element d = (Element)columns.item(i);
                            switch(d.getAttribute("name")){
                                case "otp":
                                    otp=d.getAttribute("value");
                                    break;
                                case "salt":
                                    salt_=d.getAttribute("value");
                                    break;
                                default:
                                    throw new ParseException(d.getAttribute("name"),0);
                            }
                        }
                        if((otp!=null)&&(salt_!=null)){
                            return new DRDBDevice(s,otp,salt_);
                        }
                        break;
                    case "DRDBRecord":
                        String did=null,coord=null,received=null;
                        for(int i=0;i<columns.getLength();i++){
                            Element u = (Element)columns.item(i);
                            switch(u.getAttribute("name")){
                                case "did":
                                    did=u.getAttribute("value");
                                    break;
                                case "coord":
                                    coord=u.getAttribute("value");
                                    break;
                                case "received":
                                    received=u.getAttribute("value");
                                    break;
                                default:
                                    throw new ParseException(u.getAttribute("name"),0);
                            }
                        }
                        if((did!=null)&&(coord!=null)&(received!=null)){
                            return new DRDBRecord(s,Integer.parseInt(did),coord,AuthSession.getDateFormat().parse(received));
                        }
                        break;
                    case "UDDB":
                        String uid_=null;
                        for(int i=0;i<columns.getLength();i++){
                            Element u = (Element)columns.item(i);
                            switch(u.getAttribute("name")){
                                case "uid":
                                    uid_=u.getAttribute("value");
                                    break;
                                default:
                                    throw new ParseException(u.getAttribute("name"),0);
                            }
                        }
                        if(uid_!=null){
                            return new UserDevice(s,uid_);
                        }
                        break;
                    default:
                        throw new IllegalArgumentException(table);
                }
            }
        }
        throw new ParseException("No return!",0);
    }

    @Override
    public void rmRow(String table, String key) throws ParserConfigurationException, FileNotFoundException, TransformerException {
        if((table==null)||(key==null)) return;
        NodeList tables = DOC.getElementsByTagName("table");
        Element t=null;
        boolean fin=false;
        for(int i=0;i<tables.getLength();i++){
            t=(Element)tables.item(i);
            if(t.getAttribute("name").equals(table)){
                fin=true;
                break;
            }
        }
        if((fin)&&(t!=null)){
            NodeList rows = t.getElementsByTagName("row");
            for(int i=0;i<rows.getLength();i++){
                Element r=(Element)rows.item(i);
                if(r.getAttribute("primary").equals(key)){
                    t.removeChild(r);
                    writeback();
                    break;
                }
            }
        }  
    }

    @Override
    public void setRow(String table, Row r) {
        if((table==null)||(r==null)) return;
        NodeList tables = DOC.getElementsByTagName("table");
        Element t = null;
        boolean fin=false;
        for(int i=0;i<tables.getLength();i++){
            t=(Element)tables.item(i);
            if(t.getAttribute("name").equals(table)){
                fin=true;
                break;
            }
        }
        if((fin)&&(t!=null)){  
            for(String cname:r.getColumnNames()){
                Element row = DOC.createElement("row");
                row.setAttribute(cname, r.getItem(cname));
                t.appendChild(row);
            }
        }
    }

    @Override
    public String[] getKeys(String table) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void writeback() throws ParserConfigurationException, TransformerConfigurationException, FileNotFoundException, TransformerException{
        TransformerFactory.newInstance().
                newTransformer().
                transform(
                        new DOMSource(DOC), 
                        new StreamResult(
                                new FileOutputStream(F)
                        )
                );
    }
    
}
