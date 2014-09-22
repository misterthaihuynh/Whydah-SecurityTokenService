package net.whydah.token.data.user;

public interface UserAuthenticator {
    public UserToken logonUser(String applicationTokenId, String appTokenXml, String userCredentialXml);

    UserToken createAndLogonUser(String applicationtokenid, String appTokenXml, String userCredentialXml, String fbUserXml);
}