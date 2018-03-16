
export const TYPE_OLD = 125000;
export const TYPE_WORK = 126000;
export const TYPE_HERO = 127000;



export function isOldCard(data){
	switch (data.type) {
	case TYPE_OLD:
		return true;
	default:
		return false;
	}
}
export function isStudentCard(data){
	switch (data.type) {
	case TYPE_OLD:
	case TYPE_WORK:
	case TYPE_HERO:
		return false;
	default:
		return true;
	}
}

export function getDiscardType(data){
	switch (data.type) {
	case TYPE_OLD:
		return "敬老卡号";
	case TYPE_WORK:
		return "劳模卡号";
	case TYPE_HERO:
		return "烈属卡号";
	default:
		return "学生卡号";
	}
}



export function getDiscardCardName(data){
	switch (data.type) {
	case TYPE_OLD:
		return "敬老卡";
	case TYPE_WORK:
		return "劳模卡";
	case TYPE_HERO:
		return "烈属卡";
	default:
		return "学生卡";
	}
}

export function getDiscardCardPlaceHolder(data){
	return "请输入" + getDiscardType(data);
}



