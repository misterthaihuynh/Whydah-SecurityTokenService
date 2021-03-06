package net.whydah.token.user;

import net.whydah.token.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Totto
 * @author <a href="mailto:erik-dev@fjas.no">Erik Drolshammer</a>
 */
public class UserToken implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(UserToken.class);

    private String tokenid;

    //From UIB
    private String uid;
    private String personRef;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String timestamp;

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    private String lastSeen;

    private static String defcon;
    private String securityLevel;
    private String lifespan;
    private String issuer;


    private String ns2link;
    private List<ApplicationRoleEntry> roleList;
    //Ignored properties: cellPhone,

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    private String applicationID=null;


    public UserToken() {
        this.timestamp = Long.toString(System.currentTimeMillis());
        this.lastSeen= new Date().toString();
        this.roleList = new LinkedList<>();
    }


    public boolean isValid() {
        if (timestamp == null || lifespan == null) {
            logger.trace("usertoken invalid because timestamp or lifespan is not set. timestamp={}  lifespan={}", timestamp, lifespan);
            return false;
        }

        long now = System.currentTimeMillis();
        long timeout = Long.parseLong(timestamp) + Long.parseLong(lifespan);
        boolean stillValid = timeout > now;
        if (!stillValid) {
            logger.trace("usertoken invalid (timed out). timeout={} is NOT greater than now={}", timeout, now);
        }
        logger.trace("usertoken is valid!");
        return stillValid;
    }

    public UserToken copy() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            byte[] obj = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(obj);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (UserToken) ois.readObject();
        } catch (Exception e) {
            logger.error("Error copying UserToken", e);
        }
        return null;
    }

    //Used by usertoken.ftl
    public String getMD5() {
        String md5base = null2empty(uid) + null2empty(personRef) + null2empty(tokenid) + null2empty(timestamp)
                + null2empty(firstName) + null2empty(lastName) + null2empty(email) + null2empty(securityLevel) + null2empty(issuer);
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(md5base.getBytes("UTF-8"));
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            return bigInt.toString(16);
        } catch (Exception e) {
            logger.error("Error creating MD5 hash, returning empty string. userToken: " + toString(), e);
            return "";
        }
    }
    private String null2empty(String value) {
        return value != null ? value : "";
    }


    public void addApplicationRoleEntry(ApplicationRoleEntry role) {
        roleList.add(role);
    }

    public void setTokenid(String tokenid) {
        this.tokenid = tokenid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public void setPersonRef(String personRef) {
        this.personRef = personRef;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }
    public void setLifespan(String lifespan) {
        this.lifespan = lifespan;
    }
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
    public void setRoleList(List<ApplicationRoleEntry> roleList) {
        this.roleList = roleList;
    }
    public static void setDefcon(String defcon) {
        UserToken.defcon = defcon;
    }

    public void setNs2link(String ns2link) {
        this.ns2link = ns2link;
    }


    public String getTokenid() {
        return tokenid;
    }
    public String getUid() {
        return uid;
    }
    public String getPersonRef() {
        return personRef;
    }
    public String getUserName() {
        return userName;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public String getSecurityLevel() {
        return securityLevel;
    }
    public String getLifespan() {
        return lifespan;
    }
    // TODO  return a better issuer?
    public String getIssuer() {
        if (ns2link!=null){
            return ns2link;
        }
        return issuer;
    }
    public List<ApplicationRoleEntry> getRoleList() {
        if (applicationID!=null){
            return getRoleList(applicationID);
        }
        return roleList;
    }
    public List<ApplicationRoleEntry> getRoleList(String applicationID) {
        if (UserTokenFactory.shouldReturnFullUserToken(applicationID)){
            return roleList;
        }
        List<ApplicationRoleEntry> filteredRoleList= new LinkedList<>();
        for ( ApplicationRoleEntry are: roleList) {
            if (are.getApplicationId().equalsIgnoreCase(applicationID)){
                filteredRoleList.add(are);
            }

        }
        return filteredRoleList;
    }
    public static String getDefcon() {
        return defcon;
    }
    public String getNs2link() {
        return ns2link;
    }

    @Override
    public String toString() {
        return "UserToken{" +
                "tokenid='" + tokenid + '\'' +
                ", uid='" + uid + '\'' +
                ", personRef='" + personRef + '\'' +
                ", userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", lastSeen='" + lastSeen + '\'' +
                ", securityLevel='" + securityLevel + '\'' +
                ", lifespan='" + lifespan + '\'' +
                ", issuer='" + issuer + '\'' +
                ", roleList.size=" + getRoleList().size() +
                '}';
    }
}