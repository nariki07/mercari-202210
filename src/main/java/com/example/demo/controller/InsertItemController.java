package com.example.demo.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.domain.Category;
import com.example.demo.domain.Item;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ItemRepository;

@Component
@Controller
@RequestMapping("/item")
public class InsertItemController {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ItemRepository itemRepository;

	@GetMapping("/insert")
	public String insert() {
		List<String[]> dataList = new ArrayList<>();
		// ファイルのパスを代入.
		String train_tsv = "src/main/resources/files/train.tsv";

		// ファイルのパスを引数で渡してファイルオブジェクトをインスタンス化.
		File f = new File(train_tsv);

		try (BufferedReader br = new BufferedReader(new FileReader(f));) {

			String line;
			// 1行ずつCSVファイルを読み込む

			while ((line = br.readLine()) != null) {
				String[] data = line.split("\t", 0); // １行分をタブ区切りで配列に変換.
				dataList.add(data);
			}

			// 指定した行数分だけ挿入するためのfor文.
			for (int i = 0; i < 1482534; i++) {
				String[] data = dataList.get(i);
				// categoryドメインに値をセットする. 例) dataList[0]のdata[3] = Men/Tops/T-shirts.
				String[] category2 = data[3].split("/", 0);

				if (category2.length == 1) { // カテゴリーnullを考慮している。0には""とかが入っている模様なので1にしている.
					// itemドメインのinsertを行う.
					Item item = new Item();
					item.setName(data[1]);
					item.setConditionId(Integer.parseInt(data[2]));
					item.setCategory(null);
					item.setBrand(data[4]);
					item.setPrice(Double.parseDouble(data[5]));
					item.setShipping(Integer.parseInt(data[6]));
					item.setDescription(data[7]);
					itemRepository.insert(item);
				} else {

					// 現在のレコードの小カテゴリオブジェクトを取得する.
					Category smallCategory = categoryRepository.findByNameAll(data[3]);

					// itemドメインのinsertを行う.
					Item item = new Item();
					item.setName(data[1]);
					item.setConditionId(Integer.parseInt(data[2]));
					if (smallCategory != null) {
						item.setCategory(smallCategory.getId());
					} else {
						item.setCategory(null);
					}
					item.setBrand(data[4]);
					item.setPrice(Double.parseDouble(data[5]));
					item.setShipping(Integer.parseInt(data[6]));
					item.setDescription(data[7]);
					itemRepository.insert(item);
				}
				
				
			}

		} catch (

		IOException e) {
			System.out.println(e);
		}

		return "finish";
	}

}