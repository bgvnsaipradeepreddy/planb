package com.user.planb.model;

import android.content.ContentValues;

import com.user.planb.database.SQliteTables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 1/20/2018.
 */

public class DataItems {


    /*************************************************************
     *login page model data
     * This model data is used for login page
     *************************************************************/
    private String loginStatus;
    private int loginUserId;
    private String loginUserName;
    private String loginUserStatus;
    private String loginErrorMsg;
    private List<Integer> userSelectedPlaces;
    private List<Integer> userEmeSelected;

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public int getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(int loginUserId) {
        this.loginUserId = loginUserId;
    }

    public String getLoginUserName() {
        return loginUserName;
    }

    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
    }

    public String getLoginErrorMsg() {
        return loginErrorMsg;
    }

    public void setLoginErrorMsg(String loginErrorMsg) {
        this.loginErrorMsg = loginErrorMsg;
    }

    public List<Integer> getUserSelectedPlaces() {
        return userSelectedPlaces;
    }

    public void setUserSelectedPlaces(List<Integer> userSelectedPlaces) {
        this.userSelectedPlaces = userSelectedPlaces;
    }

    public String getLoginUserStatus() {
        return loginUserStatus;
    }

    public void setLoginUserStatus(String loginUserStatus) {
        this.loginUserStatus = loginUserStatus;
    }

    public List<Integer> getUserEmeSelected() {
        return userEmeSelected;
    }

    public void setUserEmeSelected(List<Integer> userEmeSelected) {
        this.userEmeSelected = userEmeSelected;
    }

    /*************************************************************
     *state details model data
     * This model data is used for state information
     *************************************************************/
    private int state_id;
    private String state_name;

    public int getState_id() {
        return state_id;
    }

    public void setState_id(int state_id) {
        this.state_id = state_id;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    /*************************************************************
     *singup page model data
     * This model data is used for signup page
     *************************************************************/
    private String registerStatus;
    private int registerUserId;
    private String registerUserName;
    private String registerErrorMsg;

    public String getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(String registerStatus) {
        this.registerStatus = registerStatus;
    }

    public int getRegisterUserId() {
        return registerUserId;
    }

    public void setRegisterUserId(int registerUserId) {
        this.registerUserId = registerUserId;
    }

    public String getRegisterUserName() {
        return registerUserName;
    }

    public void setRegisterUserName(String registerUserName) {
        this.registerUserName = registerUserName;
    }

    public String getRegisterErrorMsg() {
        return registerErrorMsg;
    }

    public void setRegisterErrorMsg(String registerErrorMsg) {
        this.registerErrorMsg = registerErrorMsg;
    }


    /*************************************************************
     *places model data
     * This model data is used for getting places info
     *************************************************************/
    private Map<Integer, String> states = new HashMap<Integer, String>();
    private Map<Integer,Map<Integer,String>> districts = new HashMap<>();
    private Map<Integer,Map<Integer,String>> places = new HashMap<>();

    public Map<Integer, String> getStates() {
        return states;
    }

    public void setStates(Map<Integer, String> states) {
        this.states = states;
    }

    public Map<Integer, Map<Integer, String>> getPlaces() {
        return places;
    }

    public Map<Integer, Map<Integer, String>> getDistricts() {
        return districts;
    }

    public void setDistricts(Map<Integer, Map<Integer, String>> districts) {
        this.districts = districts;
    }

    public void setPlaces(Map<Integer, Map<Integer, String>> places) {
        this.places = places;
    }

    /*************************************************************
     *User selected places model data
     * This model data is used for getting user selected places info
     *************************************************************/
    private String addSelPlacesStatus;

    public String getAddSelPlacesStatus() {
        return addSelPlacesStatus;
    }

    public void setAddSelPlacesStatus(String addSelPlacesStatus) {
        this.addSelPlacesStatus = addSelPlacesStatus;
    }

    /*************************************************************
     *User selected places model data
     * This model data is used for getting user selected places info
     *************************************************************/
    private String deleteSelPlacesStatus;

    public String getDeleteSelPlacesStatus() {
        return deleteSelPlacesStatus;
    }

    public void setDeleteSelPlacesStatus(String deleteSelPlacesStatus) {
        this.deleteSelPlacesStatus = deleteSelPlacesStatus;
    }
    /*************************************************************
     *query model data
     * This model data is used for getting query info
     *************************************************************/
    private int queryId;
    private String queryUserName;
    private int queryUserId;
    private int queryPlaceId;
    private String queryDatePosted;
    private String queryTitle;
    private String queryContent;
    private String queryImageLocation;
    private String queryUserInfo;
    private String queryAnswers;
    private int queryEmergency;

    public int getQueryId() {
        return queryId;
    }

    public void setQueryId(int queryId) {
        this.queryId = queryId;
    }

    public int getQueryPlaceId() {
        return queryPlaceId;
    }

    public void setQueryPlaceId(int queryPlaceId) {
        this.queryPlaceId = queryPlaceId;
    }

    public int getQueryUserId() {
        return queryUserId;
    }

    public void setQueryUserId(int queryUserId) {
        this.queryUserId = queryUserId;
    }

    public String getQueryUserName() {
        return queryUserName;
    }

    public void setQueryUserName(String queryUserName) {
        this.queryUserName = queryUserName;
    }

    public String getQueryDatePosted() {
        return queryDatePosted;
    }

    public void setQueryDatePosted(String queryDatePosted) {
        this.queryDatePosted = queryDatePosted;
    }

    public String getQueryTitle() {
        return queryTitle;
    }

    public void setQueryTitle(String queryTitle) {
        this.queryTitle = queryTitle;
    }

    public String getQueryContent() {
        return queryContent;
    }

    public void setQueryContent(String queryContent) {
        this.queryContent = queryContent;
    }

    public String getQueryImageLocation() {
        return queryImageLocation;
    }

    public void setQueryImageLocation(String queryImageLocation) {
        this.queryImageLocation = queryImageLocation;
    }

    public String getQueryUserInfo() {
        return queryUserInfo;
    }

    public void setQueryUserInfo(String queryUserInfo) {
        this.queryUserInfo = queryUserInfo;
    }

    public String getQueryAnswers() {
        return queryAnswers;
    }

    public void setQueryAnswers(String queryAnswers) {
        this.queryAnswers = queryAnswers;
    }

    public int getQueryEmergency() {
        return queryEmergency;
    }

    public void setQueryEmergency(int queryEmergency) {
        this.queryEmergency = queryEmergency;
    }


    String userLivedPlace;
    Integer userLivedStart;
    Integer UserLivedEnd;
    Integer isItCurrentLived;

    public String getUserLivedPlace() {
        return userLivedPlace;
    }

    public void setUserLivedPlace(String userLivedPlace) {
        this.userLivedPlace = userLivedPlace;
    }

    public int getUserLivedStart() {
        return userLivedStart;
    }

    public void setUserLivedStart(int userLivedStart) {
        this.userLivedStart = userLivedStart;
    }

    public int getUserLivedEnd() {
        return UserLivedEnd;
    }

    public void setUserLivedEnd(int userLivedEnd) {
        UserLivedEnd = userLivedEnd;
    }

    public int getIsItCurrentLived() {
        return isItCurrentLived;
    }

    public void setIsItCurrentLived(int isItCurrentLived) {
        this.isItCurrentLived = isItCurrentLived;
    }

    private String queryStatus;
    public String getQueryStatus() {
        return queryStatus;
    }
    public void setQueryStatus(String queryStatus) {
        this.queryStatus = queryStatus;
    }


    private String queryExists;
    public String getQueryExists() {
        return queryExists;
    }
    public void setQueryExists(String queryExists) {
        this.queryExists = queryExists;
    }

    private Boolean answersExists;

    public Boolean getAnswersExists() {
        return answersExists;
    }

    public void setAnswersExists(Boolean answersExists) {
        this.answersExists = answersExists;
    }

    private ArrayList<String> answers;
    private ArrayList<Integer> answerIds;
    private ArrayList<String> answersUsers;
    private ArrayList<Integer> answersUserIds;

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public ArrayList<String> getAnswersUsers() {
        return answersUsers;
    }

    public void setAnswersUsers(ArrayList<String> answersUsers) {
        this.answersUsers = answersUsers;
    }

    public ArrayList<Integer> getAnswersUserIds() {
        return answersUserIds;
    }

    public void setAnswersUserIds(ArrayList<Integer> answersUserIds) {
        this.answersUserIds = answersUserIds;
    }

    public ArrayList<Integer> getAnswerIds() {
        return answerIds;
    }

    public void setAnswerIds(ArrayList<Integer> answerIds) {
        this.answerIds = answerIds;
    }

    private Boolean commentExists;

    public Boolean getCommentExists() {
        return commentExists;
    }

    public void setCommentExists(Boolean commentExists) {
        this.commentExists = commentExists;
    }


    private ArrayList<String> comments;
    private ArrayList<Integer> commentIds;
    private ArrayList<String> commentUsers;
    private ArrayList<Integer> commentUserIds;

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    public ArrayList<Integer> getCommentIds() {
        return commentIds;
    }

    public void setCommentIds(ArrayList<Integer> commentIds) {
        this.commentIds = commentIds;
    }

    public ArrayList<String> getCommentUsers() {
        return commentUsers;
    }

    public void setCommentUsers(ArrayList<String> commentUsers) {
        this.commentUsers = commentUsers;
    }

    public ArrayList<Integer> getCommentUserIds() {
        return commentUserIds;
    }

    public void setCommentUserIds(ArrayList<Integer> commentUserIds) {
        this.commentUserIds = commentUserIds;
    }

    private String Comment;
    private Integer commentId;
    private String commentUser;
    private Integer commentUserId;

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(String commentUser) {
        this.commentUser = commentUser;
    }

    public Integer getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(Integer commentUserId) {
        this.commentUserId = commentUserId;
    }


    private String poolStatus;

    public String getPoolStatus() {
        return poolStatus;
    }

    public void setPoolStatus(String poolStatus) {
        this.poolStatus = poolStatus;
    }



    private String poolExists;

    public String getPoolExists() {
        return poolExists;
    }

    public void setPoolExists(String poolExists) {
        this.poolExists = poolExists;
    }

    private int poolId;
    private String poolUserName;
    private int poolCost;
    private String poolDstAddress;
    private String poolDstPlace;
    private String poolStartAddress;
    private int poolSeats;
    private String poolStartDate;
    private String poolStartTime;
    private String poolUserComments;
    private int poolType;
    private int poolEmailVerification;
    private int poolPhoneVerification;

    public int getPoolId() {
        return poolId;
    }

    public void setPoolId(int poolId) {
        this.poolId = poolId;
    }

    public String getPoolUserName() {
        return poolUserName;
    }

    public void setPoolUserName(String poolUserName) {
        this.poolUserName = poolUserName;
    }

    public int getPoolCost() {
        return poolCost;
    }

    public void setPoolCost(int poolCost) {
        this.poolCost = poolCost;
    }

    public String getPoolDstAddress() {
        return poolDstAddress;
    }

    public void setPoolDstAddress(String poolDstAddress) {
        this.poolDstAddress = poolDstAddress;
    }

    public String getPoolDstPlace() {
        return poolDstPlace;
    }

    public void setPoolDstPlace(String poolDstPlace) {
        this.poolDstPlace = poolDstPlace;
    }

    public String getPoolStartAddress() {
        return poolStartAddress;
    }

    public void setPoolStartAddress(String poolStartAddress) {
        this.poolStartAddress = poolStartAddress;
    }

    public int getPoolSeats() {
        return poolSeats;
    }

    public void setPoolSeats(int poolSeats) {
        this.poolSeats = poolSeats;
    }

    public String getPoolStartDate() {
        return poolStartDate;
    }

    public void setPoolStartDate(String poolStartDate) {
        this.poolStartDate = poolStartDate;
    }

    public String getPoolStartTime() {
        return poolStartTime;
    }

    public void setPoolStartTime(String poolStartTime) {
        this.poolStartTime = poolStartTime;
    }

    public String getPoolUserComments() {
        return poolUserComments;
    }

    public void setPoolUserComments(String poolUserComments) {
        this.poolUserComments = poolUserComments;
    }

    public int getPoolType() {
        return poolType;
    }

    public void setPoolType(int poolType) {
        this.poolType = poolType;
    }

    public int getPoolEmailVerification() {
        return poolEmailVerification;
    }

    public void setPoolEmailVerification(int poolEmailVerification) {
        this.poolEmailVerification = poolEmailVerification;
    }

    public int getPoolPhoneVerification() {
        return poolPhoneVerification;
    }

    public void setPoolPhoneVerification(int poolPhoneVerification) {
        this.poolPhoneVerification = poolPhoneVerification;
    }
}
