package com.tck.daole.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/10.
 */

public class Bean {
    public String message;
    public int status;
    public String Head;
    public UserModels model;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public UserModels getModel() {
        return model;
    }

    public void setModel(UserModels model) {
        this.model = model;
    }



    public class ShoucangType{
        public int status;
        public String  message;
        public String collectionOfShopsStatus;  //店铺收藏状态，1为已收藏，2为未收藏
        public String  commodityStatus; //商品收藏状态，1为已收藏，2为未收藏
    }


    public class OnlineModel {
        public List<Online> list;
        public int status;
        public String message;

        public List<Online> getList() {
            return list;
        }

        public void setList(List<Online> list) {
            this.list = list;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public class Online {
        public String articleId;//=文章ID;
        public String title;//=文章标题;
        public String content;//=文章内容;
        public String publishdate;//=文章发表时间;
        public int likenumber;//=文章点赞数量;
        public String likeStatus;//=是否点赞.1点过赞，0没有点过赞;
        public String articletype;//=文章类型：1招聘求职 2二手车辆 3二手交易 4家政服务 5房产交易 6更多选择 7旅游攻略 8论坛贴吧 9自驾游召集 10生活问答 11技巧分享 12唠一唠;
        public int pageview;//=文章浏览数量;
        public int commentnumber;//=文章评论数量;

        public int getPageview() {
            return pageview;
        }

        public void setPageview(int pageview) {
            this.pageview = pageview;
        }

        public int getLikenumber() {

            return likenumber;
        }

        public void setLikenumber(int likenumber) {
            this.likenumber = likenumber;
        }

        public int getCommentnumber() {
            return commentnumber;
        }

        public void setCommentnumber(int commentnumber) {
            this.commentnumber = commentnumber;
        }

        public String getArticleId() {
            return articleId;
        }

        public void setArticleId(String articleId) {
            this.articleId = articleId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPublishdate() {
            return publishdate;
        }

        public void setPublishdate(String publishdate) {
            this.publishdate = publishdate;
        }


        public String getLikeStatus() {
            return likeStatus;
        }

        public void setLikeStatus(String likeStatus) {
            this.likeStatus = likeStatus;
        }

        public String getArticletype() {
            return articletype;
        }

        public void setArticletype(String articletype) {
            this.articletype = articletype;
        }


    }


    //商品分类列表
    public class ShopTypeContentModel {
        public List<ShopTypeContent> model;
        public int status;
        public String message;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<ShopTypeContent> getModel() {
            return model;
        }

        public void setModel(List<ShopTypeContent> model) {
            this.model = model;
        }
    }

    //商品分类列表
    public class ShopTypeContent {
        public String num;    //已选数量
        public String takeProjectId;//=商品ID
        public String name;//=商品名字
        public String detail;//=商品详情
        public String saleNum;//=商品销量
        public String price;//=商品单价
        public String projectImage;//=商品分类ID
        public String cartNum;  //加入购物车数量
        public String title; //商品分类列表介绍

        public String getCartNum() {
            return cartNum;
        }

        public void setCartNum(String cartNum) {
            this.cartNum = cartNum;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getTakeProjectId() {
            return takeProjectId;
        }

        public void setTakeProjectId(String takeProjectId) {
            this.takeProjectId = takeProjectId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getSaleNum() {
            return saleNum;
        }

        public void setSaleNum(String saleNum) {
            this.saleNum = saleNum;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getProjectImage() {
            return projectImage;
        }

        public void setProjectImage(String projectImage) {
            this.projectImage = projectImage;
        }
    }


    public class ShopTypeModel {
        public ShopType model;
        public int status;
        public String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public ShopType getModel() {
            return model;
        }

        public void setModel(ShopType model) {
            this.model = model;
        }
    }

    public class ShopType {


        public String adminStoreId;//=店铺id
        public String logoImage;//=店铺图片
        public String storeDetail;//=店铺详情
        public String storeName;//e=店铺名称
        public String sendTime; //配送时间
        public String lowestPrice;//=起送价格
        public String servicePrice;//=配送价格
        public String storeMessage; //店铺优惠
        public List<GoodsCategory> goodsCategory;   //分类列表


        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public String getStoreMessage() {
            return storeMessage;
        }

        public void setStoreMessage(String storeMessage) {
            this.storeMessage = storeMessage;
        }

        public String getSendTime() {
            return sendTime;
        }

        public void setSendTime(String sendTime) {
            this.sendTime = sendTime;
        }

        public String getAdminStoreId() {
            return adminStoreId;
        }

        public void setAdminStoreId(String adminStoreId) {
            this.adminStoreId = adminStoreId;
        }

        public String getLogoImage() {
            return logoImage;
        }

        public void setLogoImage(String logoImage) {
            this.logoImage = logoImage;
        }

        public String getStoreDetail() {
            return storeDetail;
        }

        public void setStoreDetail(String storeDetail) {
            this.storeDetail = storeDetail;
        }


        public String getLowestPrice() {
            return lowestPrice;
        }

        public void setLowestPrice(String lowestPrice) {
            this.lowestPrice = lowestPrice;
        }

        public String getServicePrice() {
            return servicePrice;
        }

        public void setServicePrice(String servicePrice) {
            this.servicePrice = servicePrice;
        }

        public List<GoodsCategory> getGoodsCategory() {
            return goodsCategory;
        }

        public void setGoodsCategory(List<GoodsCategory> goodsCategory) {
            this.goodsCategory = goodsCategory;
        }
    }

    public class GoodsCategory {
        public String GoodsCategoryId;//=商品分类ID
        public String GoodsCategoryName;//=商品分类的类名
        public String storeId;//=商品分类的归属店铺I

        public String getGoodsCategoryId() {
            return GoodsCategoryId;
        }

        public void setGoodsCategoryId(String goodsCategoryId) {
            GoodsCategoryId = goodsCategoryId;
        }

        public String getGoodsCategoryName() {
            return GoodsCategoryName;
        }

        public void setGoodsCategoryName(String goodsCategoryName) {
            GoodsCategoryName = goodsCategoryName;
        }

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }
    }

/*"adminStoreId":"1",
        "logoImage":"uploadImage/FFF.jpg",
        "storeDetail":"0000",
        "storeName":"这是家黄焖",
        "lowestPrice":5,
        "servicePrice":89,
        "sendTime":"40",
        "goodsCategory"*/

    //首页外卖团购等列表
    public class IndexBigClassModel {
        public List<IndexBigClass> model;
        public int status;
        public String message;
        public List<IndexBigClass> list;


        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<IndexBigClass> getModel() {
            return model;
        }

        public void setModel(List<IndexBigClass> model) {
            this.model = model;
        }
    }

    public class IndexBigClass {
        public String oid;//=店铺oid
        public String logoImage;//=店铺图片

        public List<ChaoShiImage> project;

        public String evaluateIdstarlevel;//=店铺星级
        public String sellnumber;//=月售
        public String name;//=店铺名称
        public String address;//=地址
        public String jvLi;//距离

        public List<ChaoShiImage> getProject() {
            return project;
        }

        public void setProject(List<ChaoShiImage> project) {
            this.project = project;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getJvLi() {
            return jvLi;
        }

        public void setJvLi(String jvLi) {
            this.jvLi = jvLi;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getLogoImage() {
            return logoImage;
        }

        public void setLogoImage(String logoImage) {
            this.logoImage = logoImage;
        }

        public String getEvaluateIdstarlevel() {
            return evaluateIdstarlevel;
        }

        public void setEvaluateIdstarlevel(String evaluateIdstarlevel) {
            this.evaluateIdstarlevel = evaluateIdstarlevel;
        }


        public String getSellnumber() {
            return sellnumber;
        }

        public void setSellnumber(String sellnumber) {
            this.sellnumber = sellnumber;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }


    public class ChaoShiImage {
        public String projectId;//=商品ID
        public String projectName;//=商品名称
        public String loanAmounts;//=商品单价
        public String projectImage;//=商品图片

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public String getLoanAmounts() {
            return loanAmounts;
        }

        public void setLoanAmounts(String loanAmounts) {
            this.loanAmounts = loanAmounts;
        }

        public String getProjectImage() {
            return projectImage;
        }

        public void setProjectImage(String projectImage) {
            this.projectImage = projectImage;
        }
    }


    public class SearchPageModel {
        public List<SearchPage> model;

        public List<SearchPage> getModel() {
            return model;
        }

        public void setModel(List<SearchPage> model) {
            this.model = model;
        }
    }

    public class SearchPage {
        public String status;//=0;//成功返回1，失败返回0
        public String oid;//=搜索id
        public String content;//=商品名称

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }


    public class PostedParticularsList {
        public List<PostedParticulars> list;

        public List<PostedParticulars> getList() {
            return list;
        }

        public void setList(List<PostedParticulars> list) {
            this.list = list;
        }
    }

    public class PostedParticulars {
        public String articleId;//=文章ID
        public String articlecommentId;//=文章评论ID
        public String nickName;//=用户呢称
        public String content;//=评论内容

        public String getArticleId() {
            return articleId;
        }

        public void setArticleId(String articleId) {
            this.articleId = articleId;
        }

        public String getArticlecommentId() {
            return articlecommentId;
        }

        public void setArticlecommentId(String articlecommentId) {
            this.articlecommentId = articlecommentId;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }


    public class PostedModel {
        public Posted model;

        public Posted getModel() {
            return model;
        }

        public void setModel(Posted model) {
            this.model = model;
        }
    }

    public class Posted {

        public String memberId;//=文章发表人ID;
        public String nickName;//=文章发表人昵称;
        public String head;//=文章发表人头像;
        public String title;//=文章标题;
        public String content;//=文章内容;
        public String publishdate;//=文章发表时间;
        public String pageview;//=浏览次数;
        public String commentnumber;//=评论个数;
        public String likeStatus;//=是否点赞.1点过赞，0没有点过赞;

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPublishdate() {
            return publishdate;
        }

        public void setPublishdate(String publishdate) {
            this.publishdate = publishdate;
        }

        public String getPageview() {
            return pageview;
        }

        public void setPageview(String pageview) {
            this.pageview = pageview;
        }

        public String getCommentnumber() {
            return commentnumber;
        }

        public void setCommentnumber(String commentnumber) {
            this.commentnumber = commentnumber;
        }

        public String getLikeStatus() {
            return likeStatus;
        }

        public void setLikeStatus(String likeStatus) {
            this.likeStatus = likeStatus;
        }
    }


    public class MyPostedListModel {

        public String message;
        public int status;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<MyPostedList> list;

        public List<MyPostedList> getList() {
            return list;
        }

        public void setList(List<MyPostedList> list) {
            this.list = list;
        }
    }

    public class MyPostedList {
        public String articleId;//=文章ID;
        public String title;//=文章标题;
        public String content;//=文章内容;
        public String publishdate;//=文章发表时间;
        public String likenumber;//=文章点赞数量;
        public String commentnumber;//=文章评论数量;

        public String getArticleId() {
            return articleId;
        }

        public void setArticleId(String articleId) {
            this.articleId = articleId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPublishdate() {
            return publishdate;
        }

        public void setPublishdate(String publishdate) {
            this.publishdate = publishdate;
        }

        public String getLikenumber() {
            return likenumber;
        }

        public void setLikenumber(String likenumber) {
            this.likenumber = likenumber;
        }

        public String getCommentnumber() {
            return commentnumber;
        }

        public void setCommentnumber(String commentnumber) {
            this.commentnumber = commentnumber;
        }
    }


//店铺信息

    public class ShopInformationModel {
        public List<ShopInformationImageList> list;
        public ShopInformation model;

        public List<ShopInformationImageList> getList() {
            return list;
        }

        public void setList(List<ShopInformationImageList> list) {
            this.list = list;
        }

        public ShopInformation getModel() {
            return model;
        }

        public void setModel(ShopInformation model) {
            this.model = model;
        }
    }

    //店铺图片


    public class ShopInformationImageList {
        public String projectName;  //图片名称
        public String projectImage; //图片地址

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public String getProjectImage() {
            return projectImage;
        }

        public void setProjectImage(String projectImage) {
            this.projectImage = projectImage;
        }
    }

    //店铺信息
    public class ShopInformation {
        public String adminsStoreId;//=店铺ID;
        public String name;//=店铺名字;
        public String storeDetail;//=店铺详情;
        public String logoImage;//=店铺头像;
        public String starlevel;//=评论星级;
        public String orderNum;//=月售;
        public String projectName;//=商品名字;
        public String projectImage;//=商品图片;
        public String address;//=店铺地址;
        public String adminsStoreStarlevel;//=店铺评价;
        public String storeMessage;//=商家信息;
        public String storeType;//=商家类型;
        public String startTime;//=开门时间;
        public String endTime;//=打烊时间;

        public String tel;  //电话

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getAdminsStoreId() {
            return adminsStoreId;
        }

        public void setAdminsStoreId(String adminsStoreId) {
            this.adminsStoreId = adminsStoreId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStoreDetail() {
            return storeDetail;
        }

        public void setStoreDetail(String storeDetail) {
            this.storeDetail = storeDetail;
        }

        public String getLogoImage() {
            return logoImage;
        }

        public void setLogoImage(String logoImage) {
            this.logoImage = logoImage;
        }

        public String getStarlevel() {
            return starlevel;
        }

        public void setStarlevel(String starlevel) {
            this.starlevel = starlevel;
        }

        public String getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public String getProjectImage() {
            return projectImage;
        }

        public void setProjectImage(String projectImage) {
            this.projectImage = projectImage;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAdminsStoreStarlevel() {
            return adminsStoreStarlevel;
        }

        public void setAdminsStoreStarlevel(String adminsStoreStarlevel) {
            this.adminsStoreStarlevel = adminsStoreStarlevel;
        }

        public String getStoreMessage() {
            return storeMessage;
        }

        public void setStoreMessage(String storeMessage) {
            this.storeMessage = storeMessage;
        }

        public String getStoreType() {
            return storeType;
        }

        public void setStoreType(String storeType) {
            this.storeType = storeType;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }


    /**
     * 评论列表
     */

    public class CommentModel {
        public List<Comment> list;
        public String message;
        public int status;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<Comment> getList() {
            return list;
        }

        public void setList(List<Comment> list) {
            this.list = list;
        }
    }

    /**
     * 评论实体类
     */

    public class Comment {
        public String projectId;  //商品id
        public String projectName; //商品名


        public String adminsStoreId;//=店铺ID;
        public String name;//=店铺名字;
        public String cryptonym;//=是否匿名（匿名返回“匿名用户”，不匿名返回“用户nickName”）;
        public String head;//=评论人头像;
        public String starlevel;//=评论星级;
        public String evaluatetime;//=评论时间;
        public String content;//=评论内容;
        public String commentLevel;//=评论等级

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public String getAdminsStoreId() {
            return adminsStoreId;
        }

        public void setAdminsStoreId(String adminsStoreId) {
            this.adminsStoreId = adminsStoreId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCryptonym() {
            return cryptonym;
        }

        public void setCryptonym(String cryptonym) {
            this.cryptonym = cryptonym;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getStarlevel() {
            return starlevel;
        }

        public void setStarlevel(String starlevel) {
            this.starlevel = starlevel;
        }

        public String getEvaluatetime() {
            return evaluatetime;
        }

        public void setEvaluatetime(String evaluatetime) {
            this.evaluatetime = evaluatetime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCommentLevel() {
            return commentLevel;
        }

        public void setCommentLevel(String commentLevel) {
            this.commentLevel = commentLevel;
        }
    }


    public class ShopNameModel {
        public ShopName model;
        public List<projectThumPath> list;//=商品图片;        ;

        public ShopName getModel() {
            return model;
        }

        public void setModel(ShopName model) {
            this.model = model;
        }
    }

    public class projectThumPath {
        public String projectId;    //团购商品id
        public String projectName;      //团购商品名
        public String projectLoanAmounts;   //团购商品价格
        public String projectThumPath;  //团购商品图片
    }

    public class ShopName {

        public String adminsStoreId;//=店铺ID;
        public String name;//=店铺名字;
        public String lowestPrice;//=店铺起送价;
        public String servicePrice;//=店铺配送费;
        public String thumPath;//=店铺图片;
        public String projectName;//=商品名字;
        public String projectLoanAmounts;//=商品价格;
        public String detail;

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getAdminsStoreId() {
            return adminsStoreId;
        }

        public void setAdminsStoreId(String adminsStoreId) {
            this.adminsStoreId = adminsStoreId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLowestPrice() {
            return lowestPrice;
        }

        public void setLowestPrice(String lowestPrice) {
            this.lowestPrice = lowestPrice;
        }

        public String getServicePrice() {
            return servicePrice;
        }

        public void setServicePrice(String servicePrice) {
            this.servicePrice = servicePrice;
        }

        public String getThumPath() {
            return thumPath;
        }

        public void setThumPath(String thumPath) {
            this.thumPath = thumPath;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public String getProjectLoanAmounts() {
            return projectLoanAmounts;
        }

        public void setProjectLoanAmounts(String projectLoanAmounts) {
            this.projectLoanAmounts = projectLoanAmounts;
        }

    }


    public class MerchandiseModel {
        public String message;
        public int status;
        public Merchandise model;

        public Merchandise getModel() {
            return model;
        }

        public void setModel(Merchandise model) {
            this.model = model;
        }
    }

    /**
     * 商品信息实体类
     */
    public class Merchandise {
        public String num; //商品已选数量
        public String oid;//商品ID;
        public String adminStoreName;//=店铺名字;
        public String projectName;//=商品名字;
        public String saleNum;//=月售;
        public String detail;//=商品详情(备注);
        public String loanAmounts;//=商品价格;
        public String introduction;//=商品介绍(商品信息);
        public String path;//=商品图片;//

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getAdminStoreName() {
            return adminStoreName;
        }

        public void setAdminStoreName(String adminStoreName) {
            this.adminStoreName = adminStoreName;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public String getSaleNum() {
            return saleNum;
        }

        public void setSaleNum(String saleNum) {
            this.saleNum = saleNum;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getLoanAmounts() {
            return loanAmounts;
        }

        public void setLoanAmounts(String loanAmounts) {
            this.loanAmounts = loanAmounts;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }


    public class AddressList {
        public List<Address> list;

        public List<Address> getList() {
            return list;
        }

        public void setList(List<Address> list) {
            this.list = list;
        }
    }

    public class Address {
        public String memberId;//用户ID
        public String nickName;//=用户呢称
        public String addressId;//=地址ID
        public String people;//=地址联系人
        public String phone;//=联系人电话
        public String province;//=联系人省
        public String city;//=联系人市
        public String district;//=联系人区
        public String street;//=联系人街道
        public String address;//=联系人地址
        public String defaultAddress;//=是否为默认地址 0是 1不是

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getAddressId() {
            return addressId;
        }

        public void setAddressId(String addressId) {
            this.addressId = addressId;
        }

        public String getPeople() {
            return people;
        }

        public void setPeople(String people) {
            this.people = people;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDefaultAddress() {
            return defaultAddress;
        }

        public void setDefaultAddress(String defaultAddress) {
            this.defaultAddress = defaultAddress;
        }
    }


    public class ShopModel {
        public List<Shop> list;
        public int status;
        public String message;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<Shop> getList() {
            return list;
        }

        public void setList(List<Shop> list) {
            this.list = list;
        }
    }

    public class Shop {

        public String memberId;//用户ID;
        public String oid;//店铺收藏ID;
        public String name;//店铺名称;
        public String address;//店铺地址;
        public String thumPath;//店铺图片;
        public String starlevel;//店铺评价星级;
        public String jvliStr;//距离;
        public String adminStoreId;     //店铺id

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getThumPath() {
            return thumPath;
        }

        public void setThumPath(String thumPath) {
            this.thumPath = thumPath;
        }

        public String getStarlevel() {
            return starlevel;
        }

        public void setStarlevel(String starlevel) {
            this.starlevel = starlevel;
        }

        public String getJvliStr() {
            return jvliStr;
        }

        public void setJvliStr(String jvliStr) {
            this.jvliStr = jvliStr;
        }
    }


    //收藏、商品列表
    public class CommodityEnshrineModel {
        public String oid;
        public List<CommodityEnshrine> list;
        public int status;
        public String message;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<CommodityEnshrine> getList() {
            return list;
        }

        public void setList(List<CommodityEnshrine> list) {
            this.list = list;
        }
    }


    //收藏、商品实体类
    public class CommodityEnshrine {

        public String memberId;
        public String projectName;
        public String drtail;
        public String saleNum;
        public String loanAmounts;
        public String thumPath;

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public String getDrtail() {
            return drtail;
        }

        public void setDrtail(String drtail) {
            this.drtail = drtail;
        }

        public String getSaleNum() {
            return saleNum;
        }

        public void setSaleNum(String saleNum) {
            this.saleNum = saleNum;
        }

        public String getLoanAmounts() {
            return loanAmounts;
        }

        public void setLoanAmounts(String loanAmounts) {
            this.loanAmounts = loanAmounts;
        }

        public String getThumPath() {
            return thumPath;
        }

        public void setThumPath(String thumPath) {
            this.thumPath = thumPath;
        }
    }


    //评价列表
    public class EvaluateModel {
        public List<Evaluate> model;

        public List<Evaluate> getModel() {
            return model;
        }

        public void setModel(List<Evaluate> model) {
            this.model = model;
        }
    }

    //评价实体类
    public class Evaluate {
        public String oid;  //oid主键
        public String content;  //评论内容
        public String starlevel;    //评论星级
        public String cryptonym;    //匿名 0匿名 1不匿名
        public String commentLevel; //评论等级 1好评 2中评 3差评
        public String evaluatetime; //评论时间

        public UserModels memberId; //用户类，获得昵称
        public EvaluateImage evaluateimageId;   //评论图片

        public EvaluateImage getEvaluateimageId() {
            return evaluateimageId;
        }

        public void setEvaluateimageId(EvaluateImage evaluateimageId) {
            this.evaluateimageId = evaluateimageId;
        }

        public UserModels getMemberId() {
            return memberId;
        }

        public void setMemberId(UserModels memberId) {
            this.memberId = memberId;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getStarlevel() {
            return starlevel;
        }

        public void setStarlevel(String starlevel) {
            this.starlevel = starlevel;
        }

        public String getCryptonym() {
            return cryptonym;
        }

        public void setCryptonym(String cryptonym) {
            this.cryptonym = cryptonym;
        }

        public String getCommentLevel() {
            return commentLevel;
        }

        public void setCommentLevel(String commentLevel) {
            this.commentLevel = commentLevel;
        }

        public String getEvaluatetime() {
            return evaluatetime;
        }

        public void setEvaluatetime(String evaluatetime) {
            this.evaluatetime = evaluatetime;
        }
    }

    //评价图片
    public class EvaluateImage {
        public String thumPath; //评价图片

        public String getThumPath() {
            return thumPath;
        }

        public void setThumPath(String thumPath) {
            this.thumPath = thumPath;
        }
    }


    //用户实体类
    public class UserModels {
        //        public List<User> model;
        public String oid;
        public int status;
        public String token;
        public String message;
        public String nickName; //昵称
        public String sex;  //性别
        public String signature;      //个性签名
        public String phone;    //手机号
        public String age;  //性别
        public String balance;  //账户余额
        public String head; //用户头像

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }

/*    public class User{
        public String token;
    }*/

    //验证码实体类
    public class YZMModel {
        public String yzm;     //验证码
        public String message;

        public String getYzm() {
            return yzm;
        }

        public void setYzm(String yzm) {
            this.yzm = yzm;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }


    //首页轮番图列表实体类
    public class BannerModel {
        public List<Banner> model;

        public List<Banner> getModel() {
            return model;
        }

        public void setModel(List<Banner> model) {
            this.model = model;
        }
    }


    //首页轮番图实体类
    public class Banner {
        public String title;    //轮番图标题
        public String imgPath;  //图片地址
        public String urlPath;  //链接地址

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public String getUrlPath() {
            return urlPath;
        }

        public void setUrlPath(String urlPath) {
            this.urlPath = urlPath;
        }
    }


    //滚动新闻列表
    public class JournalismModel {
        public List<Journalism> model;

        public List<Journalism> getModel() {
            return model;
        }

        public void setModel(List<Journalism> model) {
            this.model = model;
        }
    }

    //滚动新闻实体类
    public class Journalism {
        public String oid;
        public String title;    //新闻标题
        public String content;  //新闻内容

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    //乐拼购商品列表
    public class SpellModel {
        public List<Spell> list;
        public int status;
        public String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

    }


    //乐拼购商品详情实体类
    public class SpellParticulars {
        public String oid;
        public String name;
        public String title;
        public String detail;
        public double loanAmounts;
        public double price;
        public String saleNum;
        public List<ProjectImage> path;
        public List<SpellEveluate> evaluate;
    }

    //乐拼购商品实体类
    public class Spell {
        public int status;
        public String message;
        public String oid;  //商品主键
        public String name;  //商品名称
        public String loanAmounts;     //商品价格
        public String saleNum;  //销量
        public ProjectImage projectImage; //乐拼购图片
        public String detail;   //商品详情
        public String introduction; //商品介绍
        public List<ProjectImage> path;
        public SpellParticulars model;
        public String price;     //商品原价
        public String ThumPath; //商品图片

        //

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getName() {
            return name;
        }

        public void setName(String projectName) {
            this.name = projectName;
        }

        public String getLoanAmounts() {
            return loanAmounts;
        }

        public void setLoanAmounts(String loanAmounts) {
            this.loanAmounts = loanAmounts;
        }

        public String getSaleNum() {
            return saleNum;
        }

        public void setSaleNum(String saleNum) {
            this.saleNum = saleNum;
        }

        public ProjectImage getProjectImage() {
            return projectImage;
        }

        public void setProjectImage(ProjectImage projectImage) {
            this.projectImage = projectImage;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }


    }


    public class SpellEveluate {
        public String cryptonym;    //用户名
        public String head; //用户头像
        public String starlevel;    //评价星级
        public String evaluatetime; //评论时间
        public String content;  //评论内容
        public String commentLevel; //评价类型      评论等级 1好评 2中评 3差评

    }

    //图片实体类
    public class ProjectImage {
        public String thumPath; //乐拼购图片

        public String getThumPath() {
            return thumPath;
        }

        public void setThumPath(String thumPath) {
            this.thumPath = thumPath;
        }
    }


    //论坛贴吧文章列表
    public class ForumModel {
        public List<Forum> model;

        public List<Forum> getModel() {
            return model;
        }

        public void setModel(List<Forum> model) {
            this.model = model;
        }
    }

    //论坛贴吧文章实体类
    public class Forum {
        public String oid;  //文章主键
        public String title;    //文章标题
        public String content;  //文章内容
        public String publishdate;  //文章发布时间
        public String pageview;     //文章浏览量

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPublishdate() {
            return publishdate;
        }

        public void setPublishdate(String publishdate) {
            this.publishdate = publishdate;
        }

        public String getPageview() {
            return pageview;
        }

        public void setPageview(String pageview) {
            this.pageview = pageview;
        }
    }

    //文章点赞
    public class ArticleLike {

    }


    //景区订票列表
    public class BookingList {
        public String message;
        public int status;
        public List<Booking> list;
    }

    //景区订票详情
    public class BookingModel {
        public String message;
        public int status;
        public Booking model;
    }


    //景区票实体类
    public class Booking {
        public String takeProjectId;  //商品id
        public String takeProjectName; //购票名称
        public String title; //购票名称
        public String price;    //价格
        public String detail;    //价格
        public String saleNum;   //销售量
        public String ctime;    //开放时间
        public String ftime;    //结束试讲
        public String projectImage;   //景区订票缩略图片
        public String path;   //景区订票大图片
    }


    //优惠券列表
    public class CouponModel {
        public List<Coupon> list;

        public List<Coupon> getList() {
            return list;
        }

        public void setList(List<Coupon> list) {
            this.list = list;
        }
    }

    //优惠券实体类
    public class Coupon {
        public String status;   //登录状态
        public String couponId;  //優惠券couponId
        public String cpName;   //優惠券名稱
        public String money;    //抵扣金額
        public String fullCutMoney; //滿減金額
        public String effectiveTime;    //有效期
        public String cpDescribe;      //描述

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCouponId() {
            return couponId;
        }

        public void setCouponId(String couponId) {
            this.couponId = couponId;
        }

        public String getCpName() {
            return cpName;
        }

        public void setCpName(String cpName) {
            this.cpName = cpName;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getFullCutMoney() {
            return fullCutMoney;
        }

        public void setFullCutMoney(String fullCutMoney) {
            this.fullCutMoney = fullCutMoney;
        }

        public String getEffectiveTime() {
            return effectiveTime;
        }

        public void setEffectiveTime(String effectiveTime) {
            this.effectiveTime = effectiveTime;
        }

        public String getCpDescribe() {
            return cpDescribe;
        }

        public void setCpDescribe(String cpDescribe) {
            this.cpDescribe = cpDescribe;
        }
    }


    //系统消息列表
    public class NewsModel {
        public List<News> model;

        public List<News> getModel() {
            return model;
        }

        public void setModel(List<News> model) {
            this.model = model;
        }
    }

    //系统消息实体类
    public class News {
        public String oid;  //oid主键
        public String title;    //消息标题
        public String text; //消息内容

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }


    public class LoginInfo {
        public String message;
        public Login model;
        public int status;
    }

    public static class Login {
        public String phone;
        public String sex;
        public String nickName;
        public String age;
        public String wechat;
        public String head;
        public String Signature;
    }

    public class GameItems {
        //        public String message;
        public List<Game> item;
//        public int status;
    }

    public class Game {
        public String itemName;
        public String facePriceValue;
        public String itemId;
    }

    public class FuelItems {
        //        public String message;
        public List<Fuel> item;
//        public int status;
    }

    public class PhoneItems {
        public String message;
        public List<Fuel> model;
        public int status;
    }

    public class Fuel {
        public String inPrice;
        public String itemName;
        public String itemId;
        public String rechargeAmount;
    }


    public class ShopImagePath {
        public String Head;
        public int status;
        public String message;
    }

    public class State {
        public int status;
        public String message;
    }
}
