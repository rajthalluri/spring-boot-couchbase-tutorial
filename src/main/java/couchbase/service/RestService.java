package couchbase.service;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.json.JsonObject;
import couchbase.dao.IDatabaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class RestService {

    @Value("${hostname}")
    private String hostname;

    @Value("${bucket}")
    private String bucket;

    @Value("${password}")
    private String password;

    @Autowired
    private IDatabaseDao databaseDao;

    public @Bean
    Cluster cluster() {
        return CouchbaseCluster.create(hostname);
    }

    public @Bean
    Bucket bucket() {
        return cluster().openBucket(bucket, password);
    }

    public Object getAll() {
        return databaseDao.getAll(bucket());
    }

    public Object getByDocumentId(@RequestParam String document_id) {
        if (document_id.equals("")) {
            return new ResponseEntity<String>(JsonObject.create().put("message", "A document id is required").toString(), HttpStatus.BAD_REQUEST);
        }
        return databaseDao.getByDocumentId(bucket(), document_id);
    }

    public Object delete(@RequestBody String json) {
        JsonObject jsonData = JsonObject.fromJson(json);
        if (StringUtils.isEmpty(jsonData.getString("document_id"))) {
            return new ResponseEntity<String>(JsonObject.create().put("message", "A document id is required").toString(), HttpStatus.BAD_REQUEST);
        }
        return databaseDao.delete(bucket(), jsonData.getString("document_id"));
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
        return databaseDao.save(bucket(), jsonData);
    }
}
