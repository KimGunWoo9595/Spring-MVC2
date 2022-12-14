package hello.itemservice.web.form;

import hello.itemservice.domain.item.DeliveryCode;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/form/items")
@RequiredArgsConstructor
public class FormItemController {

    private final ItemRepository itemRepository;

    //이 클래스안에 있는 컨트롤러를 사용하면 어떠한 컨트롤러를 사용하든 model.addAttribute까지 해준다.
    @ModelAttribute("regions")
    public Map<String,String> regions(){
        //LinkedHashMap을 사용하는이류는 HashMap을 사용하면 순서가 뒤죽박죽 될 수 있다.
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("Seoul", "서울");
        regions.put("Busan", "부산");
        regions.put("JeJu", "제주");
        regions.put("All", "전국");
        return regions;  // List가 model을 통해서 넘어간다.
        //이렇게 해주면
        //model.addAttribute("regions",regions); 가 자동으로 들어가는 것이다.

        /*@ModelAttribute의 특별한 사용법
        등록 폼, 상세화면, 수정 폼에서 모두 서울, 부산, 제주라는 체크 박스를 반복해서 보여주어야 한다. 이렇게
        하려면 각각의 컨트롤러에서 model.addAttribute(...) 을 사용해서 체크 박스를 구성하는 데이터를
        반복해서 넣어주어야 한다.
        @ModelAttribute 는 이렇게 컨트롤러에 있는 별도의 메서드에 적용할 수 있다.
        이렇게하면 해당 컨트롤러를 요청할 때 regions 에서 반환한 값이 자동으로 모델( model )에 담기게 된다.
        물론 이렇게 사용하지 않고, 각각의 컨트롤러 메서드에서 모델에 직접 데이터를 담아서 처리해도 된다.*/
    }

    @ModelAttribute("itemTypes")//이렇게해주면 enum타입의 리스트가 model에의하여 view단으로 넘어간다.
    public ItemType[] itemTypes(){
        //ItemType.values() 를 사용하면 해당 ENUM의 모든 정보를 배열로 반환한다
        ItemType[] values = ItemType.values();
        return values;
    }

    //List에 객체자체를 넣어버린 유형
    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes(){
        List<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NOMAL", "일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
        return deliveryCodes;
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "form/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "form/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "form/addForm";
    }

    //상품등록 처리 비즈니스
    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        log.info("item.open={}",item.getOpen());
        log.info("item.regions={}", item.getRegions());
        log.info("item.itemType={}", item.getItemType());
        log.info("item.getDeliveryCode={}",item.getDeliveryCode());
        log.info("item 객체={}",item);
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/form/items/{itemId}"; //새로고침을 방지하기위해서
        //return 의 리다이렉트 값에 값을 적어주면 pathvariable로 들어가게되고 안쓰면 쿼리파라미터로 들어가게된다
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "form/editForm";
    }

    //수정
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        log.info("수정에서의 itemopen={}",item.getOpen());
        itemRepository.update(itemId, item);
        return "redirect:/form/items/{itemId}";
    }

    //

}

