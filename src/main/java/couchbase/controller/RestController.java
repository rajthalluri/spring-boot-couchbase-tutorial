package couchbase.controller;

import couchbase.service.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {

    @Autowired
    private RestService restService;

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public Object getAll() {
        return restService.getAll();
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public Object getByDocumentId(@RequestParam String document_id) {
        return restService.getByDocumentId(document_id);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Object delete(@RequestBody String json) {
        return restService.delete(json);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Object save(@RequestBody String json) {
        return restService.save(json);
    }
}
