package programmerzamannow.restful.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import programmerzamannow.restful.entity.User;
import programmerzamannow.restful.model.ContactResponse;
import programmerzamannow.restful.model.CreateContactRequest;
import programmerzamannow.restful.model.PagingResponse;
import programmerzamannow.restful.model.SearchContactRequest;
import programmerzamannow.restful.model.UpdateContactRequest;
import programmerzamannow.restful.model.WebResponse;
import programmerzamannow.restful.service.ContactService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.data.domain.Page;



@RestController

public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping(
        path = "/api/contacts",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
        )
    
    public WebResponse<ContactResponse> create(User user, @RequestBody CreateContactRequest request){
        ContactResponse contactResponse = contactService.create(user, request);
        return WebResponse.<ContactResponse>builder().data(contactResponse).build();
    }

    @GetMapping
    (
        path = "/api/contacts/{contactId}",
        produces = MediaType.APPLICATION_JSON_VALUE
        )
    
    public WebResponse<ContactResponse> get(User user, @PathVariable("contactId") String contactId){
        ContactResponse contactResponse = contactService.get(user, contactId);
        return WebResponse.<ContactResponse>builder().data(contactResponse).build();
    }

    @PutMapping(
        path = "/api/contacts/{contactId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
        )
    
    public WebResponse<ContactResponse> update(
                                            User user, 
                                            @RequestBody UpdateContactRequest request,
                                            @PathVariable("contactId") String contactId
                                            ){
        request.setId((contactId));

        ContactResponse contactResponse = contactService.update(user, request);
        return WebResponse.<ContactResponse>builder().data(contactResponse).build();
    }

    @DeleteMapping
    (
        path = "/api/contacts/{contactId}",
        produces = MediaType.APPLICATION_JSON_VALUE
        )
    
    public WebResponse<String> delete(User user, @PathVariable("contactId") String contactId){
        contactService.delete(user, contactId);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping
    (
    path = "/api/contacts",
    produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<ContactResponse>> search(
        User user,
        @RequestParam(value = "name", required = false) String name,
        @RequestParam(value = "email", required = false) String email,
        @RequestParam(value = "phone", required = false)String phone,
        @RequestParam(value = "page", required = false, defaultValue = "0")Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "10")Integer size
    ){
        SearchContactRequest request = SearchContactRequest.builder()
            .page(page)
            .size(size)
            .name(name)
            .email(email)
            .phone(phone)
            .build();

        Page<ContactResponse> contactResponse = contactService.search(user, request);;
        return WebResponse.<List<ContactResponse>>builder()
                .data(contactResponse.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(contactResponse.getNumber())
                        .totalPage(contactResponse.getTotalPages())
                        .size(contactResponse.getSize())
                        .build())
                .build();
    }

}
