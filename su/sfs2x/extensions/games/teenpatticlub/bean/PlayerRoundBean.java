 package su.sfs2x.extensions.games.teenpatticlub.bean;
 
 import com.smartfoxserver.v2.entities.data.ISFSObject;
 import com.smartfoxserver.v2.entities.data.SFSObject;
 import java.util.ArrayList;
 import su.sfs2x.extensions.games.teenpatticlub.utils.Appmethods;

 public class PlayerRoundBean
 {
   private String playerId;
   private ArrayList<Integer> cards = new ArrayList();
   private boolean isLeaveTable = false;
   private boolean isSeen = false;
   private boolean isPack = false;
   private boolean isShow = false;
   private boolean isShowCards = false;
   private boolean isSideShow = false;
   private boolean isSitOut = false;
   private boolean isSendSession = false;
   private boolean isSideShowSelected = false;
   private String sideShowPlayer = "null";
   private boolean isSideShowCalled = false;
   private ArrayList<Integer> handAmounts = new ArrayList();
   private int totalBetAmount = 0;
   private float wonAmount = 0.0F;
   private String lastAction = "";
   private int rank = 0;

   public ISFSObject getSFSObject()
   {
     ISFSObject sfso = new SFSObject();
     
     sfso.putUtfString("playerId", this.playerId);
     sfso.putIntArray("cards", this.cards);
     sfso.putBool("isLeaveTable", this.isLeaveTable);
     sfso.putBool("isSeen", this.isSeen);
     sfso.putBool("isPack", this.isPack);
     sfso.putBool("isShow", this.isShow);
     sfso.putBool("isSitOut", this.isSitOut);
     
     return sfso;
   }
   
   public PlayerRoundBean(String player)
   {
     this.playerId = player;
   }
   
   public boolean isSitOut() {
     return this.isSitOut;
   }
   
   public void setSitOut(boolean isSitOut) { this.isSitOut = isSitOut; }
   
   public boolean isSideShowCalled() {
     return this.isSideShowCalled;
   }
   
   public void setSideShowCalled(boolean isSideShowCalled) { this.isSideShowCalled = isSideShowCalled; }
   
   public boolean isSideShow() {
     return this.isSideShow;
   }
   
   public void setSideShow(boolean isSideShow) { this.isSideShow = isSideShow; }
   
   public boolean isSideShowSelected() {
     return this.isSideShowSelected;
   }
   
   public void setSideShowSelected(boolean isSideShowSelected) { this.isSideShowSelected = isSideShowSelected; }
   
   public String getSideShowPlayer() {
     return this.sideShowPlayer;
   }
   
   public void setSideShowPlayer(String sideShowPlayer) { this.sideShowPlayer = sideShowPlayer; }
   
   public String getLastAction() {
     return this.lastAction;
   }
   
   public void setLastAction(String lastAction) { this.lastAction = lastAction; }
   
   public int getRank() {
     return this.rank;
   }
   
   public void setRank(int rank) { this.rank = rank; }
   
   public boolean isShow() {
     return this.isShow;
   }
   
   public void setShow(boolean isShow) { this.isShow = isShow; }
   
   public boolean isShowCards() {
     return this.isShowCards;
   }
   
   public void setShowCards(boolean isShowCards) { this.isShowCards = isShowCards; }
   
   public ArrayList<Integer> getHandAmounts() {
     return this.handAmounts;
   }
   
   public void setHandAmounts(ArrayList<Integer> handAmounts) { this.handAmounts = handAmounts; }
   
   public int getTotalBetAmount() {
     return this.totalBetAmount;
   }
   
   public void setTotalBetAmount(int totalBetAmount) { this.totalBetAmount = totalBetAmount; }
   
   public String getPlayerId() {
     return this.playerId;
   }
   
   public void setPlayerId(String playerId) { this.playerId = playerId; }
   
   public ArrayList<Integer> getCards() {
     return this.cards;
   }
   
   public void setCards(ArrayList<Integer> cards) { this.cards = cards; }
   
   public float getWonAmount() {
     return this.wonAmount;
   }
   
   public void setWonAmount(float wonAmount) { this.wonAmount = Appmethods.setFloatFormat(wonAmount); }
   
   public boolean isLeaveTable() {
     return this.isLeaveTable;
   }
   
   public void setLeaveTable(boolean isLeaveTable) { this.isLeaveTable = isLeaveTable; }
   
   public boolean isSeen() {
     return this.isSeen;
   }
   
   public void setSeen(boolean isSeen) { this.isSeen = isSeen; }
   
   public boolean isPack() {
     return this.isPack;
   }
   
   public void setPack(boolean isPack) { this.isPack = isPack; }
   
   public boolean isSendSession()
   {
     return this.isSendSession;
   }
   
   public void setSendSession(boolean isSendSession) {
     this.isSendSession = isSendSession;
   }
 }


