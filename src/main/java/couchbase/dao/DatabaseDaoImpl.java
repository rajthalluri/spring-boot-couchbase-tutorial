package couchbase.dao;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.*;
import com.couchbase.client.java.query.consistency.ScanConsistency;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Repository;

import java.util.*;

import static couchbase.util.CouchBaseConstants.GET_ALL_QUERY;
import static couchbase.util.CouchBaseConstants.GET_DOCUMENT_BY_ID_QUERY;
import static couchbase.util.CouchBaseConstants.SAVE_DOCUMENT_QUERY;
import static couchbase.util.CouchBaseConstants.DELETE_DOCUMENT_BY_ID_QUERY;
import static couchbase.util.CouchBaseConstants.GET_ALL_QUERY_LIMIT_100;
import static couchbase.util.CouchBaseConstants.DEFAULT_LIMIT;

@Repository
public class DatabaseDaoImpl implements IDatabaseDao {

    @Override
    public List<Map<String, Object>> getAll(final Bucket bucket, Integer limit) {
        int queryLimit = (limit == null) ? DEFAULT_LIMIT : limit;
        N1qlQueryResult result = bucket.query(N1qlQuery.simple(String.format(GET_ALL_QUERY_LIMIT_100, bucket.name(), queryLimit),
                N1qlParams.build().consistency(ScanConsistency.REQUEST_PLUS)));
        return extractResultOrThrow(result);
    }

    @Override
    public List<Map<String, Object>> getByDocumentId(final Bucket bucket, String documentId) {
        return getParameterizedResults(bucket, String.format(GET_DOCUMENT_BY_ID_QUERY, bucket.name()), JsonArray.create().add(documentId));
    }

    @Override
    public List<Map<String, Object>> delete(final Bucket bucket, String documentId) {
        return getParameterizedResults(bucket, String.format(DELETE_DOCUMENT_BY_ID_QUERY, bucket.name()), JsonArray.create().add(documentId));
    }

    @Override
    public List<Map<String, Object>> save(final Bucket bucket, JsonObject data) {
        String documentId = !data.getString("document_id").equals("") ? data.getString("document_id") :
                UUID.randomUUID().toString();

        JsonArray parameters = JsonArray.create()
                .add(documentId)
                .add(data.getString("firstname"))
                .add(data.getString("lastname"))
                .add(data.getString("email"));

        return getParameterizedResults(bucket, String.format(SAVE_DOCUMENT_QUERY, bucket.name()), parameters);
    }

    private static List<Map<String, Object>> getParameterizedResults(final Bucket bucket, String queryStr, JsonArray parameters) {
        ParameterizedN1qlQuery query = ParameterizedN1qlQuery.parameterized(queryStr, parameters);
        N1qlQueryResult result = bucket.query(query);
        return extractResultOrThrow(result);
    }

    private static List<Map<String, Object>> extractResultOrThrow(N1qlQueryResult result) {
        if (!result.finalSuccess()) {
            throw new DataRetrievalFailureException("Query error: " + result.errors());
        }

        List<Map<String, Object>> content = new ArrayList<Map<String, Object>>();
        for (N1qlQueryRow row : result) {
            content.add(row.value().toMap());
        }
        return content;
    }
}
