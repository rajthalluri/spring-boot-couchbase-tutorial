package couchbase.util;

public class CouchBaseConstants {

    public static final String GET_ALL_QUERY = "SELECT META(users).id, firstname, lastname, email " +
            "FROM `%s` AS users";

    public static final String GET_ALL_QUERY_LIMIT_100 = "SELECT * FROM `%s` AS users limit %d";

    public static final String GET_DOCUMENT_BY_ID_QUERY = "SELECT firstname, lastname, email " +
            "FROM `%s` AS users WHERE META(users).id = $1";

    public static final String SAVE_DOCUMENT_QUERY = "UPSERT INTO `%s` (KEY, VALUE) VALUES " +
            "($1, {'firstname': $2, 'lastname': $3, 'email': $4})";

    public static final String DELETE_DOCUMENT_BY_ID_QUERY = "DELETE " +
            "FROM `%s` AS users WHERE META(users).id = $1";

    public static final int DEFAULT_LIMIT = 100;
}
