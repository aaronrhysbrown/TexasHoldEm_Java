package texasholdem;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.Random;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JList;

/*
 * Gson is released under the Apache 2.0 license.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/** Here we give a demo client for the poker game available at AIGaming.
 * We use the available API calls which can be found on the AIGaming REST API 
 * Manual page http://help.aigaming.com/rest-api-manual .
 * We have created a form that can be used to invoke these calls in order to
 * interact with the API.
 *
 * The basic idea is to create a request and response class for each API call,
 * which are used to communicate with the API and update the form.
 * The request classes' fields are the corresponding request parameters' names
 * and the response classes' fields are the corresponding result values' names.
 *
 * For example, for the "Offer Game" call we create class OfferGameReq
 * with fields BotId, BotPassword, MaximumWaitTime, GameStyleId,
 * DontPlayAgainstSameUser, DontPlayAgainstSameBot and OpponentId, and class
 * OfferGameRes with fields Result, GameState, PlayerKey and Balance.
 * 
 * Whenever we press a button to make an API call, a request object is created
 * and sent to the API using the makeAPICall method. The method serializes the
 * object to JSON, sends it to the API and returns the result, again in JSON.
 * The result is then deserialized into a result object (e.g. OfferGameRes),
 * which is used to update the form.
*/

public class MainWindow extends javax.swing.JFrame {
    
    /**
     * We use the Gson object to serialize objects into JSON format and
     * deserialize JSON strings back into the corresponding objects.
     * 
     * .serializeNulls() allows the Gson instance to output null values
     * .setPrettyPrinting() allows the Gson instance to output indented JSON text.
     * 
     * For more information: https://sites.google.com/site/gson/gson-user-guide
     */
    final Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create();
    
    private final String DEFAULTURL = "http://www.aigaming.com/Api/";
    
    private String playerKey = "";
    private int round = -1;
    private Boolean resetBets = false;
    private Boolean isWaitingForGame = false;
    private Boolean isAutoPlaying = false;
    private AutoPlayWorker autoPlayWorker;
    

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        initComponents();
        resetComponents();
    }
    
    /**
     * The GameState class represents the GameType 3: Texas Hold'Em game state
     * as described on http://help.aigaming.com/rest-api-manual
     */
    public class GameState {
        public String GameStatus;
        public int BigBlind;
        public List<String> BoardCards;
        public int DealCount;
        public int DealNumber;
        public int GameId;
        public Boolean IsDealer;
        public Boolean IsMover;
        public Boolean MandatoryResponse;
        public List<String> OpponentHand;
        public int OpponentRoundBetTotal;
        public int OpponentStack;
        public List<String> PlayerHand;
        public int PlayerRoundBetTotal;
        public int PlayerStack;
        public int PotAfterPreviousRound;
        public String ResponseDeadline;
        public int Round;
        public int SmallBlind;
        public String OpponentId;
    }

    /**
     * Create one request class and one result class for each available API call.
     */
    public class OfferGameReq {
        public String BotId;
        public String BotPassword;
        public Object MaximumWaitTime;
        public int GameStyleId;
        public Boolean DontPlayAgainstSameUser;
        public Boolean DontPlayAgainstSameBot;
        public Object OpponentId;
    }

    public class OfferGameRes {
        public String Result;
        public GameState GameState;
        public String PlayerKey;
        public int Balance;
    }
    
    public class CancelGameOfferReq {
        public String BotId;
        public String BotPassword;
        public String PlayerKey;
    }

    public class CancelGameOfferRes {
        public String Result;
        public GameState GameState;
        public int Balance;
    }
    
    public class PollForGameStateReq {
        public String BotId;
        public String BotPassword;
        public int MaximumWaitTime;
        public String PlayerKey;
    }

    public class PollForGameStateRes {
        public String Result;
        public GameState GameState;
    }
    
    public class MakeMoveReq {
        public String BotId;
        public String BotPassword;
        public String PlayerKey;
        public MoveType Move;
    }

    /**
     * MoveType class for GameType 3: Texas Hold'Em as described on
     * http://help.aigaming.com/rest-api-manual#MakeMove
     */
    public class MoveType {
        public Boolean Fold;
        public int BetSize;
    }

    public class MakeMoveRes {
        public String Result;
        public GameState GameState;
    }
    
    public class GetListOfGameStylesReq {
        public String BotId;
        public String BotPassword;
        public int GameTypeId;
    }

    /**
     * GameStyle class as described on
     * http://help.aigaming.com/rest-api-manual#GetListOfGameStyles
     */
    public class GameStyle {
        public int GameStyleId;
        public int GameType;
        public int Stake;
        public int Prize;
        public int MoveTime;
        public int Parameter1;
        public int Parameter2;
        public int Playing;
        public int Waiting;
        
        public String toString() {
            return "id: " + GameStyleId + " / " + Stake + " sat"
                     + " / " + MoveTime + " ms" + " / " + Playing + " playing, "
                    + Waiting + " waiting";
        }
    }

    public class GetListOfGameStylesRes {
        public String Result;
        public List<GameStyle> GameStyles;
        public int Balance;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        Flop1 = new javax.swing.JLabel();
        Flop2 = new javax.swing.JLabel();
        Flop3 = new javax.swing.JLabel();
        Turn = new javax.swing.JLabel();
        River = new javax.swing.JLabel();
        Hole1 = new javax.swing.JLabel();
        Hole2 = new javax.swing.JLabel();
        OppHole1 = new javax.swing.JLabel();
        OppHole2 = new javax.swing.JLabel();
        txtPot = new javax.swing.JLabel();
        txtStack = new javax.swing.JLabel();
        txtOppStack = new javax.swing.JLabel();
        txtDeal = new javax.swing.JLabel();
        iconDeal = new javax.swing.JLabel();
        iconOppDeal = new javax.swing.JLabel();
        txtBet = new javax.swing.JLabel();
        txtOppBet = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        lblPwd = new javax.swing.JLabel();
        txtPwd = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        txtBalance = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtOpp = new javax.swing.JTextField();
        chkDontPlaySameAcc = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listGameStyles = new javax.swing.JList<>();
        btnRefreshGameStyles = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        txtThinkingTime = new javax.swing.JTextField();
        chkPlayAnotherGame = new javax.swing.JCheckBox();
        btnCancelGame = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jPanel2.setBackground(new java.awt.Color(0, 128, 0));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Flop1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/back.png"))); // NOI18N
        jPanel2.add(Flop1, new org.netbeans.lib.awtextra.AbsoluteConstraints(46, 14, -1, -1));

        Flop2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/back.png"))); // NOI18N
        jPanel2.add(Flop2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 14, -1, -1));

        Flop3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/back.png"))); // NOI18N
        jPanel2.add(Flop3, new org.netbeans.lib.awtextra.AbsoluteConstraints(194, 14, -1, -1));

        Turn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/back.png"))); // NOI18N
        jPanel2.add(Turn, new org.netbeans.lib.awtextra.AbsoluteConstraints(268, 14, -1, -1));

        River.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/back.png"))); // NOI18N
        jPanel2.add(River, new org.netbeans.lib.awtextra.AbsoluteConstraints(342, 14, -1, -1));

        Hole1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/back.png"))); // NOI18N
        jPanel2.add(Hole1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 218, -1, -1));

        Hole2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/back.png"))); // NOI18N
        jPanel2.add(Hole2, new org.netbeans.lib.awtextra.AbsoluteConstraints(134, 218, -1, -1));

        OppHole1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/back.png"))); // NOI18N
        jPanel2.add(OppHole1, new org.netbeans.lib.awtextra.AbsoluteConstraints(259, 218, -1, -1));

        OppHole2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/back.png"))); // NOI18N
        jPanel2.add(OppHole2, new org.netbeans.lib.awtextra.AbsoluteConstraints(333, 218, -1, -1));

        txtPot.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        txtPot.setForeground(new java.awt.Color(255, 255, 255));
        txtPot.setText("Pot: 0");
        txtPot.setAutoscrolls(true);
        jPanel2.add(txtPot, new org.netbeans.lib.awtextra.AbsoluteConstraints(204, 115, -1, 22));

        txtStack.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        txtStack.setForeground(new java.awt.Color(255, 255, 255));
        txtStack.setText("Stack: 0");
        jPanel2.add(txtStack, new org.netbeans.lib.awtextra.AbsoluteConstraints(95, 319, -1, -1));

        txtOppStack.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        txtOppStack.setForeground(new java.awt.Color(255, 255, 255));
        txtOppStack.setText("Stack: 0");
        jPanel2.add(txtOppStack, new org.netbeans.lib.awtextra.AbsoluteConstraints(308, 319, -1, -1));

        txtDeal.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        txtDeal.setForeground(new java.awt.Color(255, 255, 255));
        txtDeal.setText("0 / 0");
        jPanel2.add(txtDeal, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 330, -1, -1));

        iconDeal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/deal.png"))); // NOI18N
        jPanel2.add(iconDeal, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 174, -1, -1));

        iconOppDeal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/deal.png"))); // NOI18N
        jPanel2.add(iconOppDeal, new org.netbeans.lib.awtextra.AbsoluteConstraints(268, 174, -1, -1));

        txtBet.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        txtBet.setForeground(new java.awt.Color(255, 255, 255));
        txtBet.setText("Bet: 0");
        jPanel2.add(txtBet, new org.netbeans.lib.awtextra.AbsoluteConstraints(104, 179, -1, -1));

        txtOppBet.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        txtOppBet.setForeground(new java.awt.Color(255, 255, 255));
        txtOppBet.setText("Bet: 0");
        jPanel2.add(txtOppBet, new org.netbeans.lib.awtextra.AbsoluteConstraints(312, 179, -1, -1));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(60, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );

        jLabel5.setText("Bot ID:");

        lblPwd.setText("Password: ");

        btnLogin.setText("Login");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        btnLogout.setText("Logout");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        txtBalance.setText("Balance: ");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Game Style Selection");

        jLabel9.setText("Specify Opponent (optional):");

        chkDontPlaySameAcc.setText("Don't play another bot in same user account as me");

        jLabel10.setText("Double click game style line to play");

        listGameStyles.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listGameStyles.setToolTipText("");
        listGameStyles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listGameStylesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(listGameStyles);

        btnRefreshGameStyles.setText("Refresh Game Styles");
        btnRefreshGameStyles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshGameStylesActionPerformed(evt);
            }
        });

        jLabel11.setText("Add \"Thinking Time\" (ms):");

        txtThinkingTime.setText("1500");
        txtThinkingTime.setToolTipText("");

        chkPlayAnotherGame.setText("Play another game when complete");

        btnCancelGame.setText("Cancel Game Offer");
        btnCancelGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelGameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(33, 33, 33)
                                    .addComponent(lblPwd))
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(28, 28, 28)
                                .addComponent(txtOpp, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(chkDontPlaySameAcc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10)
                            .addComponent(jScrollPane2)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(btnRefreshGameStyles))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel11)
                        .addGap(44, 44, 44)
                        .addComponent(txtThinkingTime, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(chkPlayAnotherGame))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(btnCancelGame)))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtPwd, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnLogout)
                        .addGap(11, 11, 11)
                        .addComponent(btnLogin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(128, 128, 128))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPwd)
                    .addComponent(btnLogin)
                    .addComponent(txtBalance)
                    .addComponent(txtPwd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLogout))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtOpp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chkDontPlaySameAcc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRefreshGameStyles)
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(txtThinkingTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(chkPlayAnotherGame)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancelGame))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>                        

    /** Reset the values of the client's components, ready for login. */
    private void resetComponents() {
        txtId.setText("");
        txtId.setEnabled(true);
        lblPwd.setVisible(true);
        txtPwd.setText("");
        txtPwd.setVisible(true);
        txtBalance.setText("Balance: ");
        txtOpp.setText("");
        txtThinkingTime.setText("1500");
        btnLogin.setVisible(true);
        btnLogout.setVisible(false);
        btnCancelGame.setVisible(false);
        chkDontPlaySameAcc.setSelected(false);
        chkPlayAnotherGame.setSelected(false);
        listGameStyles.setModel(new javax.swing.DefaultListModel());
        
        playerKey = "";
        isWaitingForGame = false;
        isAutoPlaying = false;
        
        resetGameBoard();
    }
    
    /** Reset all the components at the green game board. */
    private void resetGameBoard() {
        Hole1.setVisible(false);
        Hole2.setVisible(false);
        OppHole1.setVisible(false);
        OppHole2.setVisible(false);
        Flop1.setVisible(false);
        Flop2.setVisible(false);
        Flop3.setVisible(false);
        Turn.setVisible(false);
        River.setVisible(false);
        txtPot.setVisible(false);
        iconDeal.setVisible(false);
        iconOppDeal.setVisible(false);
        txtBet.setVisible(false);
        txtOppBet.setVisible(false);
        txtStack.setVisible(false);
        txtOppStack.setVisible(false);
        txtDeal.setVisible(false);
        
        Hole1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/back.png")));
        Hole2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/back.png")));
        OppHole1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/back.png")));
        OppHole2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/back.png")));
        Flop1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/back.png")));
        Flop2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/back.png")));
        Flop3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/back.png")));
        Turn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/back.png")));
        River.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/back.png")));
        txtPot.setText("Pot: 0");
        txtBet.setText("Bet: 0");
        txtOppBet.setText("Bet: 0");
        txtStack.setText("Stack: 0");
        txtOppStack.setText("Stack: 0");
        txtDeal.setText("0 / 0");
        round = -1;
        resetBets = false;
    }
    
    /** Initialize the game board for a new deal. */
    private void initDeal() {
        // Display the two players' hands.
        Hole1.setVisible(true);
        Hole2.setVisible(true);
        OppHole1.setVisible(true);
        OppHole2.setVisible(true);
        // Hide all the board cards.
        Flop1.setVisible(false);
        Flop2.setVisible(false);
        Flop3.setVisible(false);
        Turn.setVisible(false);
        River.setVisible(false);
        // Hide the dealer chip.
        iconDeal.setVisible(false);
        iconOppDeal.setVisible(false);
        // Show the initial bets (they should be the small and big blind.
        txtBet.setVisible(true);
        txtOppBet.setVisible(true);
        // Show the pot, players' stacks and the deal number.
        txtPot.setVisible(true);
        txtStack.setVisible(true);
        txtOppStack.setVisible(true);
        txtDeal.setVisible(true);
        
        // Reset helper variables.
        round = -1;
        resetBets = false;
    }
    
    /** Update pot and players' stacks given a new game state. */
    private void updatePotAndStacks(GameState gs) {
        txtPot.setText("Pot: " + gs.PotAfterPreviousRound);
        txtStack.setText("Stack: " + gs.PlayerStack);
        txtOppStack.setText("Stack: " + gs.OpponentStack);
    }
    
    /** Update board cards given a new game state. */
    private void updateBoardCards(GameState gs) {
        // If there are no board cards do nothing.
        if(gs.BoardCards.isEmpty()) return;
        // Otherwise show all the flop cards.
        Flop1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/" + gs.BoardCards.get(0) + ".png")));
        Flop2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/" + gs.BoardCards.get(1) + ".png")));
        Flop3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/" + gs.BoardCards.get(2) + ".png")));
        Flop1.setVisible(true);
        Flop2.setVisible(true);
        Flop3.setVisible(true);
        // If only the flop cards are present, return. Otherwise show the turn card.
        if(gs.BoardCards.size() < 4) return;
        Turn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/" + gs.BoardCards.get(3) + ".png")));
        Turn.setVisible(true);
        // If only the flop and turn cards are present, return. Otherwise show the river card.
        if(gs.BoardCards.size() < 5) return;
        River.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/" + gs.BoardCards.get(4) + ".png")));
        River.setVisible(true);
    }
        
    /** Draw the game board given a new game state. */
    private void drawGameBoard(GameState gs) {
        // If it is a new deal, reset the board cards, update the hole cards and deal number.
        if(!txtDeal.getText().equals(gs.DealNumber + " / " + gs.DealCount)) {
            // Update the hole cards.
            Hole1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/" + gs.PlayerHand.get(0) + ".png")));
            Hole2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/" + gs.PlayerHand.get(1) + ".png")));
            txtDeal.setText(gs.DealNumber + " / " + gs.DealCount);
            // Initialize the deal.
            initDeal();
            // Set the bets to the small and big blinds, respectively.
            txtBet.setText("Bet: "+gs.PlayerRoundBetTotal);
            txtOppBet.setText("Bet: "+gs.OpponentRoundBetTotal);
            // Update the dealer chip.
            if(gs.IsDealer) iconDeal.setVisible(true);
            else iconOppDeal.setVisible(true);
        }
        
        if(round != gs.Round) {
            round = gs.Round;
            if(gs.Round == 0) return;
            resetBets = true;
            
            int bet = Integer.parseInt(txtBet.getText().split("Bet: ")[1]);
            int betOpp = Integer.parseInt(txtOppBet.getText().split("Bet: ")[1]);
            
            if(bet < betOpp) txtBet.setText(txtOppBet.getText());
            else txtOppBet.setText(txtBet.getText());
            txtBet.setVisible(true);
            txtOppBet.setVisible(true);
            
            if(gs.Round != 4) return;
            OppHole1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/" + gs.OpponentHand.get(0) + ".png")));
            OppHole2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/" + gs.OpponentHand.get(1) + ".png")));
            
        } else if(resetBets) {
            if(!gs.IsMover.equals(gs.IsDealer)) return;
            updatePotAndStacks(gs);     
            updateBoardCards(gs);
            resetBets = false;
            txtBet.setText("Bet: 0");
            txtOppBet.setText("Bet: 0");
            txtBet.setVisible(false);
            txtOppBet.setVisible(false);
        } else if(!gs.IsMover) { 
            txtBet.setText("Bet: " + gs.PlayerRoundBetTotal);
            txtBet.setVisible(true);
            updatePotAndStacks(gs);        
            updateBoardCards(gs);
        } else {
            txtOppBet.setText("Bet: " + gs.OpponentRoundBetTotal);
            txtOppBet.setVisible(true);
            updatePotAndStacks(gs);        
            updateBoardCards(gs);
        }
    }
    
    /**
     * Make an http call to the specified url using the information provided by
     * the req object.
     */
    private String makeAPICall(String url, Object req) {
        // Update the request text field in the form with the corresponding
        // request information and set the response text field to empty string.
        try {       
            // Save the start time of the call in order to output the total time
            // it takes to complete the communication with the API.
            long startTime = System.currentTimeMillis();
            
            // Create a connection to the specified URL.
            URL obj = new URL(url);
            HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();

            // We are going to send a POST request with parameters in JSON
            // format and receive a response in JSON.
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            con.setRequestProperty("Accept", "application/json");

            // Get the JSON parameters from the request object.
            String urlParameters = gson.toJson(req);

            // Send the request.
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            // Get the response code and initialize variables for the response.
            int responseCode = con.getResponseCode();
            StringBuffer response = new StringBuffer();
            BufferedReader in = null;
            
            // If he response code is 200 then read from the input stream;
            // otherwise, read from the error stream.
            try {
            	in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } catch (Exception exception) {
                if (responseCode != 200) {
                	in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
            }
            
            // Read the response.
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
            }
            in.close();
            
            // Calculate the elapsed time for the whole communication.
            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            
            // Update the response box in the form and return the JSON response.
            return response.toString();
            
        } catch (Exception exception) {
            return (exception.getMessage());
        }
    }
    
    /**
     * A helper method that makes the "Poll For Game State" call and updates the
     * form.
     */
    PollForGameStateRes pollForGameState(String id, String pwd, String key) {
        // Create a request object.
        PollForGameStateReq req = new PollForGameStateReq();
        req.BotId = id;
        req.BotPassword = pwd;
        req.PlayerKey = key;

        // Make and API call and update the form.
        String resJson = makeAPICall(DEFAULTURL+"PollforGameState", req);
        PollForGameStateRes res = gson.fromJson(resJson, PollForGameStateRes.class);
        if(res.Result.equals("SUCCESS")) drawGameBoard(res.GameState);
        return res;
    }
    
    /**
     * A helper method that makes the "Make Move" call and updates the
     * form.
     */
    MakeMoveRes makeMove(String id, String pwd, String key, Boolean fold, int betsize) {
        // Create a request object.
        MakeMoveReq req = new MakeMoveReq();
        req.BotId = id;
        req.BotPassword = pwd;
        req.PlayerKey = key;
        req.Move = new MoveType();
        req.Move.Fold = fold;
        req.Move.BetSize = betsize;

        // Make and API call and update the form.
        String resJson = makeAPICall(DEFAULTURL+"MakeMove", req);
        MakeMoveRes res = gson.fromJson(resJson, MakeMoveRes.class);
        
        if(res.Result.equals("SUCCESS")) drawGameBoard(res.GameState);
        return res;
    }
    
    /** Swing Worker helper class that allows us to play the game in a separate thread. */
    class AutoPlayWorker extends javax.swing.SwingWorker<Void, GameState> {
        private int GameStyleId;
        
        private GameState offerAndWaitForGame() {
            // Offer a game.
            String result = offerGame(GameStyleId);
            if(result.equals("Invited Bot Not Existing")) return null;
            
            // Show the cancel game button since the player is waiting for a game.
            btnCancelGame.setText("Cancel Game");
            isWaitingForGame = true;        
            btnCancelGame.setVisible(true);
            // Poll for the game state initially and play only if the result is
            // "SUCCESS", i.e, if we are taking part in a game.
            PollForGameStateRes pfgsres;
            String Result = "";
            do {
                pfgsres = pollForGameState(txtId.getText(), String.valueOf(txtPwd.getPassword()), playerKey);
                Result = pfgsres.Result;
            } while(Result.equals("WAITING_FOR_GAME") && isWaitingForGame);
            if (!Result.equals("SUCCESS")) return null;
            GameState gs = pfgsres.GameState;
            publish(gs);
            return gs;
        }
        
        private void autoPlayGame(GameState gs) {
            // Now a game has started so change the cancel game button to "Stop Game".
            btnCancelGame.setText("Stop Game");
            isWaitingForGame = false;
            isAutoPlaying = true;
            
            PollForGameStateRes pfgsres;
            String Result = "";
            
            // Play until possible; the loop breaks if the game is over.
            while(isAutoPlaying) {
                // If it is our turn, send a move request and update the form.
                if (gs.IsMover) {
                    
                    try {
                        // Wait for the specified thinking time.
                        int thinkingTime = 0;
                        try {
                            thinkingTime = (int) Double.parseDouble(txtThinkingTime.getText());
                            if(thinkingTime < 0) thinkingTime = 0;
                        } catch(Exception e) {}
                        Thread.sleep(thinkingTime);
                        
                        TexasHoldEmMove move = TexasHoldEm.calculateMove(gs);

                        // Make the move.
                        MakeMoveRes mmres = makeMove(txtId.getText(), String.valueOf(txtPwd.getPassword()), playerKey, move.fold, move.betsize);
                        Result = mmres.Result;
                        // Terminate the loop if the request was unsuccessful or if
                        // the game is not running anymore.
                        if (!Result.equals("SUCCESS")) break;
                        gs = mmres.GameState;
                        publish(gs);
                        if (!gs.GameStatus.equals("RUNNING")) break;
                    } catch (Exception exception) {
                        System.err.println("Caught Exception: " + exception.getMessage());
                    }
                }
                // If it is not our turn, poll for the game state and update it if 
                // the poll request was successful. Otherwise, terminate the loop.
                else {
                    pfgsres = pollForGameState(txtId.getText(), String.valueOf(txtPwd.getPassword()), playerKey);
                    Result = pfgsres.Result;
                    if (!Result.equals("SUCCESS")) break;
                    gs = pfgsres.GameState;
                    publish(gs);
                }
            }
        }
        
        public AutoPlayWorker(int GameStyleId) {
            this.GameStyleId = GameStyleId;
        }
        
        @Override
        protected Void doInBackground() {
            do {
                GameState gs = offerAndWaitForGame();
                if(gs == null) return null;
                autoPlayGame(gs);
            } while(chkPlayAnotherGame.isSelected() && isAutoPlaying);
            return null;
        }
        
        @Override
        protected void process(List<GameState> gameStates) {
            for(GameState gs : gameStates) drawGameBoard(gs);
        }
        
        @Override
        public void done() {
            // After we have finished playing the game, reset the game board.
            resetGameBoard();
            // Reset the cancel game button.
            btnCancelGame.setVisible(false);
            isAutoPlaying = false;
            // Reactivate the list with game styles.
            listGameStyles.setCellRenderer(new javax.swing.JList<>().getCellRenderer());
        }
    }
    
    /**
     * This method performs an "Offer Game" call to the API
     * and updates the form based on the result.
     */
    private String offerGame(int GameStyleId) {                                             
        // Create a new OfferGameReq object and initialize it with the current
        // values from the form.
        OfferGameReq req = new OfferGameReq();
        req.BotId = txtId.getText();
        req.BotPassword = String.valueOf(txtPwd.getPassword());
//        req.GameStyleId = (int) Double.parseDouble(txtStyle.getText());
        req.GameStyleId = GameStyleId;

        req.DontPlayAgainstSameBot = null;
        req.DontPlayAgainstSameUser = chkDontPlaySameAcc.isSelected();
        if (!(txtOpp.getText().equals("")))
            req.OpponentId = txtOpp.getText();

        // Make an "Offer Game" API call with the request object to the current
        // URL in the form.
        String result = makeAPICall(DEFAULTURL + "OfferGame", req);
        
        // result is in JSON format, so we need to deserialize it into
        // OfferGameRes object.
        OfferGameRes res = gson.fromJson(result, OfferGameRes.class);
        
        playerKey = res.PlayerKey;
        return res.Result;
    }
    
     /**
     * This method performs a "Cancel Game Offer" call to the API
     * and updates the form based on the result.
     * It is completely analogous to the "Offer Game" call.
     */
    private void cancelGameOffer() {                                                   
        // Create a request object.
        CancelGameOfferReq req = new CancelGameOfferReq();
        req.BotId = txtId.getText();
        req.BotPassword = String.valueOf(txtPwd.getPassword());
        req.PlayerKey = playerKey;

        // Make and API call and update the form.
        String resJson = makeAPICall(DEFAULTURL + "CancelGameOffer", req);
        CancelGameOfferRes res = gson.fromJson(resJson, CancelGameOfferRes.class);
        
        playerKey = "";
    }     
    
    
    private void refreshGameStyles() {
        // Create a request object.
        GetListOfGameStylesReq req = new GetListOfGameStylesReq();
        req.BotId = txtId.getText();
        req.BotPassword = String.valueOf(txtPwd.getPassword());
        req.GameTypeId = 3;

        // Make and API call and update the form.
        String resJson = makeAPICall(DEFAULTURL + "GetListOfGameStyles", req);
        GetListOfGameStylesRes res = gson.fromJson(resJson, GetListOfGameStylesRes.class);
        
        // Update the form if the request was successful.
        if(!res.Result.equals("SUCCESS")) return;
        // Update the balance.
        txtBalance.setText("Balance: " + res.Balance);
        // Update the list of game styles.
        javax.swing.DefaultListModel listModel = new javax.swing.DefaultListModel();
        for(GameStyle gs : res.GameStyles) listModel.addElement(gs.toString());
        listGameStyles.setModel(listModel);
    }    

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // Login by making a "Get List Of Game Styles" call to the API.
        
        // Create a request object.
        GetListOfGameStylesReq req = new GetListOfGameStylesReq();
        req.BotId = txtId.getText();
        req.BotPassword = String.valueOf(txtPwd.getPassword());
        req.GameTypeId = 3;

        // Make and API call and update the form.
        String resJson = makeAPICall(DEFAULTURL + "GetListOfGameStyles", req);
        GetListOfGameStylesRes res = gson.fromJson(resJson, GetListOfGameStylesRes.class);

        // Update the form if the request was successful.
        if(!res.Result.equals("SUCCESS")) return;
        // Disable the user id filed, hide the password and swap the login/logout buttons.
        txtId.setEnabled(false);
        lblPwd.setVisible(false);
        txtPwd.setVisible(false);
        btnLogin.setVisible(false);
        btnLogout.setVisible(true);
        // Update the balance.
        txtBalance.setText("Balance: " + res.Balance);
        // Update the list of game styles.
        javax.swing.DefaultListModel listModel = new javax.swing.DefaultListModel();
        for(GameStyle gs : res.GameStyles) listModel.addElement(gs.toString());
        listGameStyles.setModel(listModel);
    }                                        

    private void listGameStylesMouseClicked(java.awt.event.MouseEvent evt) {                                            
        // Make a game offer if an item of the list is double clicked and
        // the player is not waiting or playing.
        javax.swing.JList listGameStyles = (javax.swing.JList) evt.getSource();
        if(evt.getClickCount() != 2 || isWaitingForGame || isAutoPlaying) return;
        int index = listGameStyles.locationToIndex(evt.getPoint());
        if (index < 0) return;
        
        // Disable the list selection.
        listGameStyles.setCellRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, false, false);
                return this;
            }
        });

        // Get the selected item.
        Object o = listGameStyles.getModel().getElementAt(index);

        // Offer a game.
        int GameStyleId = Integer.parseInt(o.toString().split("id: ")[1].split(" /")[0]);

        // Start the game in another thread.
        autoPlayWorker = new AutoPlayWorker(GameStyleId);
        autoPlayWorker.execute();
    }                                           

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {                                          
        resetComponents();
    }                                         

    private void btnCancelGameActionPerformed(java.awt.event.ActionEvent evt) {                                              
        // If the player is waiting for a game, kill the worker thread,
        // send a "Cancel Game Offer" request to the API and hide the cancel game button.
        if(isWaitingForGame) {
            autoPlayWorker.cancel(true);
            cancelGameOffer();
            btnCancelGame.setVisible(false);
            isWaitingForGame = false;
        }
        // If the player is playing, kill the worker thread, send a "Cancel Game Offer"
        // request to the API and hide the cancel game button.
        else if(isAutoPlaying) {
            autoPlayWorker.cancel(true);
            btnCancelGame.setVisible(false);
            btnCancelGame.setText("Cancel Game Offer");
            isAutoPlaying = false;
        }
    }                                             

    private void btnRefreshGameStylesActionPerformed(java.awt.event.ActionEvent evt) {                                                     
        refreshGameStyles();
    }                                                    

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JLabel Flop1;
    private javax.swing.JLabel Flop2;
    private javax.swing.JLabel Flop3;
    private javax.swing.JLabel Hole1;
    private javax.swing.JLabel Hole2;
    private javax.swing.JLabel OppHole1;
    private javax.swing.JLabel OppHole2;
    private javax.swing.JLabel River;
    private javax.swing.JLabel Turn;
    private javax.swing.JButton btnCancelGame;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnRefreshGameStyles;
    private javax.swing.JCheckBox chkDontPlaySameAcc;
    private javax.swing.JCheckBox chkPlayAnotherGame;
    private javax.swing.JLabel iconDeal;
    private javax.swing.JLabel iconOppDeal;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblPwd;
    private javax.swing.JList<String> listGameStyles;
    private javax.swing.JLabel txtBalance;
    private javax.swing.JLabel txtBet;
    private javax.swing.JLabel txtDeal;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtOpp;
    private javax.swing.JLabel txtOppBet;
    private javax.swing.JLabel txtOppStack;
    private javax.swing.JLabel txtPot;
    private javax.swing.JPasswordField txtPwd;
    private javax.swing.JLabel txtStack;
    private javax.swing.JTextField txtThinkingTime;
    // End of variables declaration                   
}
