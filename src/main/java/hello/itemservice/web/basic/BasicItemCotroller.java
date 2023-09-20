package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemCotroller {

    private final ItemRepository itemRepository;

//    @RequiredArgsConstructor가 생성자를 대신 만들어줌
//    @Autowired
//    public BasicItemCotroller(ItemRepository itemRepository) {
//        this.itemRepository = itemRepository;
//    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findVById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    /**
     * Get이나 Post 인지에 따라 기능이 다름
     *
     */

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

//    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model) {
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }

    /**
     * @ModelAttribute("item") = item객체를 생성하고 set을 자동으로 호출하여 채워줌 또한 자동으로 model에 추가해 줌
     * @param item
     * @param model
     * @return
     */

//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, Model model) {

        itemRepository.save(item);

//        model.addAttribute("item", item); // @ModelAttribute가 자동으로 추가해줌

        return "basic/item";
    }

// Item -> item 으로 model에 추가
//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, Model model) {

        itemRepository.save(item);

//        model.addAttribute("item", item);

        return "basic/item";
    }

// String이나 Int 같은 기본형이 아니면 @ModelAttribute 생략 가능
    @PostMapping("/add")
    public String addItemV4(Item item) {

        itemRepository.save(item);

//        model.addAttribute("item", item);

        return "basic/item";
    }

// 수정 폼으로 이동
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findVById(itemId);
        model.addAttribute("item", item);

        return "basic/editForm";
    }

// 수정 처리
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute("item") Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }


    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));

    }

}
