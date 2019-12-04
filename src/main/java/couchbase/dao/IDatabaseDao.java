package couchbase.dao;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.json.JsonObject;

import java.util.List;
import java.util.Map;

public interface IDatabaseDao {

    List<Map<String, Object>> getAll(final Bucket bucket);

    List<Map<String, Object>> getByDocumentId(final Bucket bucket, String documentId);

    List<Map<String, Object>> delete(final Bucket bucket, String documentId);

    List<Map<String, Object>> save(final Bucket bucket, JsonObject data);
}
