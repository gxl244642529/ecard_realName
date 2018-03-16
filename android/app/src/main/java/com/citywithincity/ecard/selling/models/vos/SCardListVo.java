package com.citywithincity.ecard.selling.models.vos;

import com.citywithincity.interfaces.IJsonValueObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 获取售卡列表
 * 
 */

public class SCardListVo implements Serializable ,IJsonValueObject {
	/**
	 *
	 */
	private static final long serialVersionUID = 3582268172295275454L;
	private int id;
	private String title;
	private int price;
	private String thumb;


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getThumb() {
		return thumb;
	}
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}

	public String getFormatPrice(){
		return String.format("%.02f", (float)price/100);
	}

	/**
	 * ID=23102, TITLE=曾先森异形卡（支持自行车开卡）, PRICE=44000, THUMB=http://218.5.80.23:18000/xls/96782fe3-08bb-4bc9-a776-f0c619711b60.jpg}, {ID=25103, TITLE=迪士尼软胶大白卡, PRICE=6400, THUMB=http://218.5.80.23:18000/xls/f55c3170-c28d-4e73-be44-c4eb8a13d8ab.jpg}, {ID=26101, TITLE=迪士尼软胶蜘蛛侠卡, PRICE=6400, THUMB=http://218.5.80.23:18000/xls/b15afcc9-7fad-445d-ae14-deb035d372e2.jpg}, {ID=26103, TITLE=迪士尼软胶米妮卡, PRICE=6400, THUMB=http://218.5.80.23:18000/xls/3cb9a105-d0ed-449e-8a2c-576d93c967d4.jpg}, {ID=26102, TITLE=迪士尼软胶米奇卡, PRICE=6400, THUMB=http://218.5.80.23:18000/xls/f551ed8a-6eaa-48c9-b184-ba689dcad652.jpg}, {ID=19111, TITLE=DIY卡黑凤梨, PRICE=3900, THUMB=http://218.5.80.23:18000/xls/83822617-f163-4ade-9bc5-0be4ab810c55.jpg}, {ID=25115, TITLE=闽南神兽冻尾雕e通卡, PRICE=3900, THUMB=http://218.5.80.23:18000/xls/96af9dfa-ee95-4b56-b03e-325c738e0495.jpg}, {ID=25117, TITLE=闽南神兽画虎兰e通卡, PRICE=3900, THUMB=http://218.5.80.23:18000/xls/349daafc-afe7-4f13-8977-1ca735df9003.jpg}, {ID=26100, TITLE=闽南神兽巴肚妖e通卡, PRICE=3900, THUMB=http://218.5.80.23:18000/xls/a022003f-7455-4d4e-8efb-a8ab090ede26.jpg}, {ID=25114, TITLE=闽南神兽肖渣貘e通卡, PRICE=3900, THUMB=http://218.5.80.23:18000/xls/997be558-45e4-49a0-a253-16b3f20d3aa2.jpg}, {ID=25116, TITLE=闽南神兽水渣貊e通卡, PRICE=3900, THUMB=http://218.5.80.23:18000/xls/c19b462f-462f-48ed-860d-75df7c39c5fe.jpg}, {ID=19106, TITLE=曾先森异形卡, PRICE=3500, THUMB=http://218.5.80.23:18000/xls/48283e99-1c1d-4689-a09a-f656bb3107ab.jpg}, {ID=19105, TITLE=曾姑凉异形卡, PRICE=3500, THUMB=http://218.5.80.23:18000/xls/94c299a4-7bc0-4b3b-95af-f895d7ed7e60.jpg}, {ID=25109, TITLE=虎见-福建-黄色异形卡, PRICE=3500, THUMB=http://218.5.80.23:18000/xls/e56cf321-85eb-406c-bbd4-9b0ab1834f7a.jpg}, {ID=25110, TITLE=虎见-福建-蓝色异形卡, PRICE=3500, THUMB=http://218.5.80.23:18000/xls/f76c7ec2-ef76-4bb1-af76-439941c5a871.jpg}, {ID=25108, TITLE=虎见-福建-红色异形卡, PRICE=3500, THUMB=http://218.5.80.23:18000/xls/271bd5b2-57af-42e1-8163-b83e8374746c.jpg}, {ID=25102, TITLE=迷彩猴, PRICE=3500, THUMB=http://218.5.80.23:18000/xls/92408114-9be3-4765-8c75-99f095019eab.jpg}, {ID=17102, TITLE=曾厝垵标准卡, PRICE=3000, THUMB=http://218.5.80.23:18000/xls/3e9da22a-7f0f-451a-bbcc-7eaaa945ae94.jpg}, {ID=25106, TITLE=虎见-福建纪念卡-黄色标准卡, PRICE=3000, THUMB=http://218.5.80.23:18000/xls/e82e1193-0bc7-446c-936d-563f2a70ee73.jpg}, {ID=19104, TITLE=我在鼓浪屿, PRICE=3000, THUMB=http://218.5.80.23:18000/xls/5c011d50-9738-4534-80f7-9b1233a35faf.jpg}]}

	 * @param json
	 * @throws JSONException
     */
	@Override
	public void fromJson(JSONObject json) throws JSONException {
		id = json.getInt("ID");
		title = json.getString("TITLE");
		price = json.getInt("PRICE");
		thumb = json.getString("THUMB");
	}
}
