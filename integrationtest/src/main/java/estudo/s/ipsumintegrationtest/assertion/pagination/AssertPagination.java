package estudo.s.ipsumintegrationtest.assertion.pagination;

import io.restassured.response.ValidatableResponse;
import static org.hamcrest.Matchers.*;


public class AssertPagination {

    public static final String HREF = ".href";
    public static final String LINKS = "_links";

    public static final String LINKS_FIRST = LINKS+".first";
    public static final String LINKS_FIRST_HREF = LINKS_FIRST+HREF;

	public static final String LINKS_PREV = LINKS+".prev";
	public static final String LINKS_PREV_HREF = LINKS_PREV+HREF;

	public static final String LINKS_SELF = LINKS+".self";
	public static final String LINKS_SELF_HREF = LINKS_SELF+HREF;

	public static final String LINKS_NEXT = LINKS+".next";
	public static final String LINKS_NEXT_HREF = LINKS_NEXT+HREF;

	public static final String LINKS_CREATE = LINKS+".create";
    public static final String LINKS_CREATE_HREF = LINKS_CREATE+HREF;

	public static final String PAGE = "page";

    public static final String PAGE_SIZE = PAGE+".size";
    public static final String PAGE_TOTAL_ELEMENTS = PAGE+".totalElements";
    public static final String PAGE_TOTAL_PAGES = PAGE+".totalPages";
    public static final String PAGE_NUMBER = PAGE+".number";




    private Links links;

    private Page page;

    public AssertPagination(Page page, Links links) {
        this.page = page;
        this.links = links;
    }

    public ValidatableResponse validate(ValidatableResponse response) {

        ValidatableResponse lastResponse = response;

        if(links != null) {

            lastResponse = response.body(LINKS, notNullValue());

            if(links.getFirst() != null) {
                lastResponse = response.body(LINKS_FIRST, notNullValue());
                lastResponse = response.body(LINKS_FIRST_HREF, equalTo(links.getFirst()));
            
            } else {
                lastResponse = response.body(LINKS_FIRST, nullValue());
            }

            if(links.getPrev() != null) {
                lastResponse = response.body(LINKS_PREV, notNullValue());
                lastResponse = response.body(LINKS_PREV_HREF, equalTo(links.getPrev()));
            
            } else {
                lastResponse = response.body(LINKS_PREV, nullValue());
            }

            if(links.getSelf() != null) {
                lastResponse = response.body(LINKS_SELF, notNullValue());
                lastResponse = response.body(LINKS_SELF_HREF, equalTo(links.getSelf()));
            
            } else {
                lastResponse = response.body(LINKS_SELF, nullValue());
            }

            if(links.getNext() != null) {
                lastResponse = response.body(LINKS_NEXT, notNullValue());
                lastResponse = response.body(LINKS_NEXT_HREF, equalTo(links.getNext()));
            
            } else {
                lastResponse = response.body(LINKS_NEXT, nullValue());
            }

            if(links.getCreate() != null) {
                lastResponse = response.body(LINKS_CREATE, notNullValue());
                lastResponse = response.body(LINKS_CREATE_HREF, equalTo(links.getCreate()));
            
            } else {
                lastResponse = response.body(LINKS_CREATE, nullValue());
            }


        } else { 
            lastResponse = response.body(LINKS, nullValue());
        }

        if(page != null) {
            lastResponse = response.body(PAGE, notNullValue());

            lastResponse = response.body(PAGE_SIZE, equalTo(page.getSize()));
            lastResponse = response.body(PAGE_TOTAL_ELEMENTS, equalTo(page.getTotalElements()));
            lastResponse = response.body(PAGE_TOTAL_PAGES, equalTo(page.getTotalPages()));
            lastResponse = response.body(PAGE_NUMBER, equalTo(page.getNumber()));
        
        } else {
            lastResponse = response.body(PAGE, nullValue());
        }

        return lastResponse;

    }


}
