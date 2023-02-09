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
@RequestMapping("/category")
public class InsertCategoryController {

	@Autowired
	public CategoryRepository categoryRepository;

	@Autowired
	public ItemRepository itemRepository;

	@GetMapping("/insert")
	public String insert() {
		List<String[]> dataList = new ArrayList<>();
		// ファイルのパスを代入.
		String train_tsv = "src/main/resources/files/train.tsv";
		// ファイルのパスを引数で渡してファイルオブジェクトをインスタンス化.
		File f = new File(train_tsv);
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line;
			// 1行ずつCSVファイルを読み込む

			while ((line = br.readLine()) != null) {
				String[] data = line.split("\t", 0); // １行分をタブ区切りで配列に変換.
				dataList.add(data);
			}

			// 指定した行数分だけ挿入するためのfor文.
			for (int i = 0; i < dataList.size(); i++) {
				String[] data = dataList.get(i);
				// categoryドメインに値をセットする. 例) dataList[0]のdata[3] = Men/Tops/T-shirts.
				String[] category2 = data[3].split("/", 0);

				if (category2.length == 1) { // カテゴリーnullを考慮している。0には""とかが入っている模様なので1にしている.
					// name_allが空です.
				} else { // 大カテゴリの処理.

					// 大カテゴリ名で検索をかける.
					Category checkLargeCategory = categoryRepository.findByNameLargeCategory(category2[0]);

					// まだ、インサートされていないカテゴリ名の場合はインサートを行う.
					if (checkLargeCategory == null) {
						Category largeCategory = new Category();
						largeCategory.setParentId(null); // 大カテゴリはparentId = null.
						largeCategory.setCategoryName(category2[0]); // category2[0]は大カテゴリーのカテゴリー名が入っている.
						largeCategory.setNameAll(null); // 大カテゴリはcategoryName = null.
						checkLargeCategory = categoryRepository.insert(largeCategory); // 自動採番されたIDが含まれたオブジェクトが返ってくる.
					}

					// 中カテゴリ名で検索をかける.
					Category checkMediumCategory = categoryRepository.findByNameMidiumCategory(category2[1]);

					// まだ、インサートされていないカテゴリ名の場合はインサートを行う.
					if (checkMediumCategory == null) {
						checkLargeCategory = categoryRepository.findByNameLargeCategory(category2[0]); // setParentId用
						Category mediumCategory = new Category();
						mediumCategory.setParentId(checkLargeCategory.getId()); // 大カテゴリのidをセット.
						mediumCategory.setCategoryName(category2[1]); // category[1]は中カテゴリーのカテゴリー名が入っている.
						mediumCategory.setNameAll(null);// 中カテゴリはcategoryName = null.
						checkMediumCategory = categoryRepository.insert(mediumCategory); // 自動採番されたIDが含まれたオブジェクトが返ってくる.
					} else if (checkMediumCategory != null) { // 空でない場合の処理.

						// 同じ中カテゴリだがMen/tops/とwomen/tops/は別物としてインサートを行わないといけない.
						// 中カテゴリ名で検索をかけ、該当するオブジェクトをリストで受け取る.
						List<Category> checkMediumCategoryList = categoryRepository
								.allFindByNameMidiumCategory(category2[1]);

						List<Integer> parentIdList = new ArrayList<>();

						// 該当する中カテゴリのすべてのparentIdをリストに格納.
						for (Category checkMediumCategory2 : checkMediumCategoryList) {
							parentIdList.add(checkMediumCategory2.getParentId());
						}

						// 全中カテゴリの親IDと大カテゴリのIDが一致しない場合のみ新たにインサートを行う.
						if (!(parentIdList.contains(checkLargeCategory.getId()))) {
							Category mediumCategory = new Category();
							mediumCategory.setParentId(checkLargeCategory.getId()); // 大カテゴリのidをセット.
							mediumCategory.setCategoryName(category2[1]); // category[1]は中カテゴリーのカテゴリー名が入っている.
							mediumCategory.setNameAll(null);// 中カテゴリはcategoryName = null.
							mediumCategory = categoryRepository.insert(mediumCategory); // 自動採番されたIDが含まれたオブジェクトが返ってくる.
						}
					}

					// 小カテゴリで検索をかける.
					Category checkSmallcategory = categoryRepository.findByNameSmallCategory(category2[2]);

					// まだ、インサートされていないカテゴリ名の場合はインサートを行う.
					if (checkSmallcategory == null) {
						checkMediumCategory = categoryRepository.findByNameMidiumCategory(category2[1]);
						Category smallCategory = new Category();
						smallCategory.setParentId(checkMediumCategory.getId()); // 中カテゴリのidをセット.
						smallCategory.setCategoryName(category2[2]); // category[2]は小カテゴリーのカテゴリー名が入っている.
						smallCategory.setNameAll(data[3]); // 全カテゴリ名をセット. Men/Tops/T-shirts.
						checkSmallcategory = categoryRepository.insert(smallCategory);
					} else if (checkSmallcategory != null) {

						// 同じ小カテゴリだがMen/tops/T-shirtsとwomen/tops/T-shirtsは別物としてインサートを行わないといけない.
						List<Category> checkSmallCategoryList = categoryRepository
								.allFindByNameSmallCategory(category2[2]);
						List<String> smallNameAllList = new ArrayList<>();

						// 検索をかけた小カテゴリすべてのname_allの文字列をリストに格納.
						for (Category checkSmallCategory2 : checkSmallCategoryList) {
							smallNameAllList.add(checkSmallCategory2.getNameAll());
						}

						// 小カテゴリのname_allの文字列とdata[3]の文字列が一致しない場合は新たにインサートを行う.
						if (!(smallNameAllList.contains(data[3]))) {
							Category smallCategory = new Category();
							smallCategory.setParentId(checkMediumCategory.getId()); // 中カテゴリのidをセット.
							smallCategory.setCategoryName(category2[2]); // category[2]は小カテゴリーのカテゴリー名が入っている.
							smallCategory.setNameAll(data[3]);
							checkSmallcategory = categoryRepository.insert(smallCategory); // 自動採番されたIDが含まれたオブジェクトが返ってくる.
						}
					}

				}

				if (category2.length == 1) { // カテゴリーnullを考慮している。0には""とかが入っている模様なので1にしている.
					// name_allが空です.
				} else {
					Category checkSmallcategory = categoryRepository.findByNameSmallCategory(category2[2]);

					// itemドメインのinsertを行う.
					Item item = new Item();
					item.setName(data[1]);
					item.setConditionId(Integer.parseInt(data[2]));
					item.setCategory(checkSmallcategory.getId());
					item.setBrand(data[4]);
					item.setPrice(Double.parseDouble(data[5]));
					item.setShipping(Integer.parseInt(data[6]));
					item.setDescription(data[7]);
					itemRepository.insert(item);
				}

			}
			// リソースの解放.try-with-resouces利用したため不必要.
//			br.close();
		} catch (

		IOException e) {
			System.out.println(e);
		}
		return "finish";
	}
}
