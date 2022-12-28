package com.example.demo.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.domain.Original;
import com.example.demo.repository.OriginalRepository;

@Controller
@RequestMapping("/")
public class InsertOriginalController {
	
	@Autowired
	private OriginalRepository originalRepository;
	
	@GetMapping("insert")
	public String insert() {
		try {

			List<String[]> dataList = new ArrayList<>();

			// ファイルのパスを代入.
			String train_tsv = "src/main/resources/files/train.tsv";

			// ファイルのパスを引数で渡してファイルオブジェクトをインスタンス化.
			File f = new File(train_tsv);

			// ファイルオブジェクトを引数に渡してFileReaderをインスタンス化.
			// さらにBufferedReaderの引数にFileReaderを渡してインスタンス化.
			BufferedReader br = new BufferedReader(new FileReader(f));

			String line;
			// 1行ずつCSVファイルを読み込む

			// 指定した行数分だけ読み込むためのfor文.
			for (int i = 0; i <= 1482534; i++) {
				line = br.readLine();
				String[] data = line.split("\t", 0);
				dataList.add(data);
			}

			/*
			 * 全件読み込むwhile文. while ((line = br.readLine()) != null) { String[] data =
			 * line.split("\t", 0); // １行分をタブ区切りで配列に変換.
			 * 
			 * for (int i = 0; i <= 7; i++) { System.out.println(data[i]); }
			 * dataList.add(data);
			 * 
			 * }
			 */

			Original original = new Original();

			// 指定した行数分だけ挿入するためのfor文.
			for (int i = 1264242; i <= 1482534; i++) {

				String[] data = dataList.get(i);

				// originalドメインに値をセットするwhile文.
				int j = 0;
				while (j <= 7) {
					if (j == 0) {
						// ID
						original.setId(Integer.parseInt(data[j]));
					} else if (j == 1) {
						// 名前
						original.setName(data[j]);
					} else if (j == 2) {
						// 状態ID
						original.setConditionId(Integer.parseInt(data[j]));
					} else if (j == 3) {
						// カテゴリー名
						original.setCategoryName(data[j]);
					} else if (j == 4) {
						// ブランド名
						original.setBrand(data[j]);
					} else if (j == 5) {
						// 価格(double型)
						original.setPrice(Double.parseDouble(data[j]));
					} else if (j == 6) {
						// 運送方法
						original.setShipping(Integer.parseInt(data[j]));
					} else if (j == 7) {
						// 説明
						original.setDescription(data[j]);
					}
					j++;
				}

				originalRepository.insert(original);

			}
			// リソースの解放.
			br.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		
		return "finish";
	}
	
}
