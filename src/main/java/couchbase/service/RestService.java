package couchbase.service;

import com.couchbase.client.java.document.json.JsonObject;
import couchbase.config.CouchbaseConfig;
import couchbase.dao.IDatabaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RestService {

    @Autowired
    private IDatabaseDao databaseDao;

    @Autowired
    private CouchbaseConfig couchbaseConfig;

    private final ResponseEntity badRequestResponseEntity =
            new ResponseEntity<String>(JsonObject.create()
                    .put("message", "A document id is required").toString(), HttpStatus.BAD_REQUEST);

    public Object getAll(Integer limit) {
        return databaseDao.getAll(couchbaseConfig.bucket(), limit);
    }

    public Object getByDocumentId(String document_id) {
        return (StringUtils.isEmpty(document_id)) ? badRequestResponseEntity :
                databaseDao.getByDocumentId(couchbaseConfig.bucket(), document_id);
    }

    public Object delete(String json) {
        JsonObject jsonData = JsonObject.fromJson(json);
        if (StringUtils.isEmpty(jsonData.getString("document_id"))) {
            return badRequestResponseEntity;
        }
        return databaseDao.delete(couchbaseConfig.bucket(), jsonData.getString("document_id"));
    }

    public Object save(String json) {
        JsonObject jsonData = JsonObject.fromJson(json);
        if (jsonData.getString("firstname") == null || jsonData.getString("firstname").equals("")) {
            return new ResponseEntity<String>(JsonObject.create().put("message", "A firstname is required").toString(), HttpStatus.BAD_REQUEST);
        } else if (jsonData.getString("lastname") == null || jsonData.getString("lastname").equals("")) {
            return new ResponseEntity<String>(JsonObject.create().put("message", "A lastname is required").toString(), HttpStatus.BAD_REQUEST);
        } else if (jsonData.getString("email") == null || jsonData.getString("email").equals("")) {
            return new ResponseEntity<String>(JsonObject.create().put("message", "An email is required").toString(), HttpStatus.BAD_REQUEST);
        }
        return databaseDao.save(couchbaseConfig.bucket(), jsonData);
    }
}
