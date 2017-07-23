package com.thek.tictactoe;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView iv_pic[] = new ImageView[10];
    int get_player1 = 0;
    int get_player2 = 0;
    int get_cpu = 0;
    int tp;
    int[] winpoint_player1 = new int[6];
    int[] winpoint_player2 = new int[6];
    int[] winpoint_cpu = new int[6];
    boolean changeside = true;
    boolean player1winthegame = false;
    boolean player2winthegame = false;
    boolean cpuwinthegame = false;
    boolean tiegame = false;

    boolean isp1andp2 = false;
    boolean isp1andcpu = false;
    boolean player1first;

    FloatingActionButton fab_plus, fab_twoplayers, fab_withcpu;
    Animation FabOpen, FabClose, FabClockwise, FabRanticlockwise;
    boolean isopen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FVBI();

        fab_plus.setOnClickListener(new FabSelect());
        fab_twoplayers.setOnClickListener(new WhenP1vsP2());
        fab_withcpu.setOnClickListener(new WhenP1vsCpu());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        StartSetting();
    }

    public void FVBI() {
        iv_pic[1] = (ImageView) findViewById(R.id.iv_pic1);
        iv_pic[2] = (ImageView) findViewById(R.id.iv_pic2);
        iv_pic[3] = (ImageView) findViewById(R.id.iv_pic3);
        iv_pic[4] = (ImageView) findViewById(R.id.iv_pic4);
        iv_pic[5] = (ImageView) findViewById(R.id.iv_pic5);
        iv_pic[6] = (ImageView) findViewById(R.id.iv_pic6);
        iv_pic[7] = (ImageView) findViewById(R.id.iv_pic7);
        iv_pic[8] = (ImageView) findViewById(R.id.iv_pic8);
        iv_pic[9] = (ImageView) findViewById(R.id.iv_pic9);
        fab_plus = (FloatingActionButton) findViewById(R.id.fab_plus);
        fab_twoplayers = (FloatingActionButton) findViewById(R.id.fab_twoplayers);
        fab_withcpu = (FloatingActionButton) findViewById(R.id.fab_withcpu);
        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        FabClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        FabRanticlockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);
    }

    private void StartSetting() {
        for (int r = 1; r < 10; r++) {
            iv_pic[r].setOnClickListener(new OnClickEvent());
        }
        for (int re = 0; re < winpoint_player1.length; re++) {
            winpoint_player1[re] = 16;
            winpoint_player2[re] = 16;
            winpoint_cpu[re] = 16;
        }
    }

    private class OnClickEvent implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            tp = Integer.parseInt(view.getTag().toString());
            if (isp1andp2) {
                P1vsP2Event();
            } else if (isp1andcpu) {
                P1vsCpuEvent(view);
            }
            DeterminationHowToWin();
        }
    }

    private class FabSelect implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (isopen) {
                fab_twoplayers.startAnimation(FabClose);
                fab_withcpu.startAnimation(FabClose);
                fab_plus.startAnimation(FabRanticlockwise);
                fab_twoplayers.setClickable(false);
                fab_withcpu.setClickable(false);
                isopen = false;
            } else {
                fab_twoplayers.startAnimation(FabOpen);
                fab_withcpu.startAnimation(FabOpen);
                fab_plus.startAnimation(FabClockwise);
                fab_twoplayers.setClickable(true);
                fab_withcpu.setClickable(true);
                isopen = true;
            }
        }
    }

    private class WhenP1vsP2 implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            isp1andp2 = true;
            isp1andcpu = false;
            GameOver();
        }
    }

    private class WhenP1vsCpu implements View.OnClickListener {

        @Override
        public void onClick(final View view) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Would you want first?")
                    .setIcon(R.drawable.crown)
                    .setCancelable(false)
                    .setPositiveButton("Sure!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            player1first = true;
                            changeside = true;
                        }
                    })
                    .setNegativeButton("Second!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            player1first = false;
                            changeside = false;
                            CpuPlaytheGame(view);

                        }
                    })
                    .show();
            isp1andp2 = false;
            isp1andcpu = true;
            GameOver();
        }
    }

    public void P1vsP2Event() {
        if (changeside && iv_pic[tp].getDrawable() == null) {
            winpoint_player1[get_player1] = tp;
            iv_pic[tp].setImageResource(R.drawable.circle);
            get_player1++;
            changeside = false;
        } else if (!changeside && iv_pic[tp].getDrawable() == null) {
            winpoint_player2[get_player2] = tp;
            iv_pic[tp].setImageResource(R.drawable.cross);
            get_player2++;
            changeside = true;
        }
    }

    public void P1vsCpuEvent(View v) {
        if (changeside && iv_pic[tp].getDrawable() == null) {
            winpoint_player1[get_player1] = tp;
            if (player1first) {
                iv_pic[tp].setImageResource(R.drawable.circle);
            } else {
                iv_pic[tp].setImageResource(R.drawable.cross);
            }
            get_player1++;
            changeside = false;
        }
        if (player1first) {
            if (get_cpu <= 3) {
                Log.d("pass " , "pass ");
                CpuPlaytheGame(v);
            }
        } else {
            if (get_cpu <= 4) {
                Log.d("pass " , "pass ");
                CpuPlaytheGame(v);
            }
        }
    }

    public void DeterminationHowToWin() {
        Winpoint_Player1_If();
        Winpoint_Player2_If();
        Winpoint_Cpu_If();
        Winpoint_Tie_If();
        if (player1winthegame) {
            new AlertDialog.Builder(this)
                    .setTitle("Player1 Win!")
                    .setIcon(R.drawable.crown)
                    .setPositiveButton("Thanks", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            GameOver();
                        }
                    })
                    .setCancelable(false)
                    .show();
        } else if (player2winthegame) {
            new AlertDialog.Builder(this)
                    .setTitle("Player2 Win!")
                    .setIcon(R.drawable.crown)
                    .setPositiveButton("Thanks", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            GameOver();
                        }
                    })
                    .setCancelable(false)
                    .show();
        } else if (cpuwinthegame) {
            new AlertDialog.Builder(this)
                    .setTitle("Cpu Win!")
                    .setIcon(R.drawable.crown)
                    .setPositiveButton("Thanks", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            GameOver();
                        }
                    })
                    .setCancelable(false)
                    .show();
        } else if (tiegame) {
            new AlertDialog.Builder(this)
                    .setTitle("It's a tie game!")
                    .setIcon(R.drawable.crown)
                    .setPositiveButton("Thanks", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            GameOver();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }

    private void Winpoint_Player1_If() {
        if (get_player1 > 2) {
            if (winpoint_player1[0] + winpoint_player1[1] + winpoint_player1[2] == 15) {
                player1winthegame = true;
            } else if (winpoint_player1[0] + winpoint_player1[1] + winpoint_player1[3] == 15) {
                player1winthegame = true;
            } else if (winpoint_player1[0] + winpoint_player1[1] + winpoint_player1[4] == 15) {
                player1winthegame = true;
            } else if (winpoint_player1[0] + winpoint_player1[2] + winpoint_player1[3] == 15) {
                player1winthegame = true;
            } else if (winpoint_player1[0] + winpoint_player1[2] + winpoint_player1[4] == 15) {
                player1winthegame = true;
            } else if (winpoint_player1[1] + winpoint_player1[2] + winpoint_player1[3] == 15) {
                player1winthegame = true;
            } else if (winpoint_player1[1] + winpoint_player1[2] + winpoint_player1[4] == 15) {
                player1winthegame = true;
            } else if (winpoint_player1[2] + winpoint_player1[3] + winpoint_player1[4] == 15) {
                player1winthegame = true;
            }
        }
    }

    private void Winpoint_Player2_If() {
        if (get_player2 > 2) {
            if (winpoint_player2[0] + winpoint_player2[1] + winpoint_player2[2] == 15) {
                player2winthegame = true;
            } else if (winpoint_player2[0] + winpoint_player2[1] + winpoint_player2[3] == 15) {
                player2winthegame = true;
            } else if (winpoint_player2[0] + winpoint_player2[1] + winpoint_player2[4] == 15) {
                player2winthegame = true;
            } else if (winpoint_player2[0] + winpoint_player2[2] + winpoint_player2[3] == 15) {
                player2winthegame = true;
            } else if (winpoint_player2[0] + winpoint_player2[2] + winpoint_player2[4] == 15) {
                player2winthegame = true;
            } else if (winpoint_player2[1] + winpoint_player2[2] + winpoint_player2[3] == 15) {
                player2winthegame = true;
            } else if (winpoint_player2[1] + winpoint_player2[2] + winpoint_player2[4] == 15) {
                player2winthegame = true;
            } else if (winpoint_player2[2] + winpoint_player2[3] + winpoint_player2[4] == 15) {
                player2winthegame = true;
            }
        }
    }

    private void Winpoint_Cpu_If() {
        if (get_cpu > 2) {
            if (winpoint_cpu[0] + winpoint_cpu[1] + winpoint_cpu[2] == 15) {
                cpuwinthegame = true;
            } else if (winpoint_cpu[0] + winpoint_cpu[1] + winpoint_cpu[3] == 15) {
                cpuwinthegame = true;
            } else if (winpoint_cpu[0] + winpoint_cpu[1] + winpoint_cpu[4] == 15) {
                cpuwinthegame = true;
            } else if (winpoint_cpu[0] + winpoint_cpu[2] + winpoint_cpu[3] == 15) {
                cpuwinthegame = true;
            } else if (winpoint_cpu[0] + winpoint_cpu[2] + winpoint_cpu[4] == 15) {
                cpuwinthegame = true;
            } else if (winpoint_cpu[1] + winpoint_cpu[2] + winpoint_cpu[3] == 15) {
                cpuwinthegame = true;
            } else if (winpoint_cpu[1] + winpoint_cpu[2] + winpoint_cpu[4] == 15) {
                cpuwinthegame = true;
            } else if (winpoint_cpu[2] + winpoint_cpu[3] + winpoint_cpu[4] == 15) {
                cpuwinthegame = true;
            }
        }
    }

    private void Winpoint_Tie_If() {
        if (get_player1 > 4 || get_player2 > 4 || get_cpu > 4) {
            if (iv_pic != null) {
                tiegame = true;
            }
        }
    }

    public void GameOver() {
        for (int cl = 1; cl < iv_pic.length; cl++) {
            iv_pic[cl].setImageDrawable(null);
        }
        for (int cl = 0; cl < winpoint_player1.length; cl++) {
            winpoint_player1[cl] = 16;
            winpoint_player2[cl] = 16;
            winpoint_cpu[cl] = 16;
        }
        changeside = true;
        player1winthegame = false;
        player2winthegame = false;
        cpuwinthegame = false;
        tiegame = false;
        player1first = true;
        get_player1 = 0;
        get_player2 = 0;
        get_cpu = 0;

        fab_twoplayers.startAnimation(FabClose);
        fab_withcpu.startAnimation(FabClose);
        fab_plus.startAnimation(FabRanticlockwise);
        fab_twoplayers.setClickable(false);
        fab_withcpu.setClickable(false);
        isopen = false;
    }

    public void CpuPlaytheGame(View v) {
        if (!changeside && !player1winthegame) {
            int a_player = winpoint_player1[0] + winpoint_player1[1];
            int b_player = winpoint_player1[0] + winpoint_player1[2];
            int c_player = winpoint_player1[0] + winpoint_player1[3];
            int d_player = winpoint_player1[0] + winpoint_player1[4];
            int e_player = winpoint_player1[1] + winpoint_player1[2];
            int f_player = winpoint_player1[1] + winpoint_player1[3];
            int g_player = winpoint_player1[1] + winpoint_player1[4];
            int h_player = winpoint_player1[2] + winpoint_player1[3];
            int i_player = winpoint_player1[2] + winpoint_player1[4];
            int j_player = winpoint_player1[3] + winpoint_player1[4];
            int a_cpu = winpoint_cpu[0] + winpoint_cpu[1];
            int b_cpu = winpoint_cpu[0] + winpoint_cpu[2];
            int c_cpu = winpoint_cpu[0] + winpoint_cpu[3];
            int d_cpu = winpoint_cpu[0] + winpoint_cpu[4];
            int e_cpu = winpoint_cpu[1] + winpoint_cpu[2];
            int f_cpu = winpoint_cpu[1] + winpoint_cpu[3];
            int g_cpu = winpoint_cpu[1] + winpoint_cpu[4];
            int h_cpu = winpoint_cpu[2] + winpoint_cpu[3];
            int i_cpu = winpoint_cpu[2] + winpoint_cpu[4];
            int j_cpu = winpoint_cpu[3] + winpoint_cpu[4];

            int ran = (int) (Math.random() * 9 + 1);
            int ran_corner = (int) ((Math.random() * 4 + 1)) * 2;
            int ran_side = (int) ((Math.random() * 4 + 1)) * 2 - 1;

            //如果自己在一排中已經佔有兩個格子，在接續的棋步必須下在剩餘的另一格空格中，才可以贏。
            if ((iv_pic[1].getDrawable() == null) && (15 - a_cpu == 1 || 15 - b_cpu == 1 || 15 - c_cpu == 1 || 15 - d_cpu == 1 || 15 - e_cpu == 1 || 15 - f_cpu == 1 || 15 - g_cpu == 1 || 15 - h_cpu == 1 || 15 - i_cpu == 1 || 15 - j_cpu == 1)) {
                Log.d("cpuwanna", "winby 1");
                winpoint_cpu[get_cpu] = 1;
                if (player1first) {
                    iv_pic[1].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[1].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            } else if ((iv_pic[2].getDrawable() == null) && (15 - a_cpu == 2 || 15 - b_cpu == 2 || 15 - c_cpu == 2 || 15 - d_cpu == 2 || 15 - e_cpu == 2 || 15 - f_cpu == 2 || 15 - g_cpu == 2 || 15 - h_cpu == 2 || 15 - i_cpu == 2 || 15 - j_cpu == 2)) {
                Log.d("cpuwanna", "winby 2");
                winpoint_cpu[get_cpu] = 2;
                if (player1first) {
                    iv_pic[2].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[2].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            } else if ((iv_pic[3].getDrawable() == null) && (15 - a_cpu == 3 || 15 - b_cpu == 3 || 15 - c_cpu == 3 || 15 - d_cpu == 3 || 15 - e_cpu == 3 || 15 - f_cpu == 3 || 15 - g_cpu == 3 || 15 - h_cpu == 3 || 15 - i_cpu == 3 || 15 - j_cpu == 3)) {
                Log.d("cpuwanna", "winby 3");
                winpoint_cpu[get_cpu] = 3;
                if (player1first) {
                    iv_pic[3].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[3].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            } else if ((iv_pic[4].getDrawable() == null) && (15 - a_cpu == 4 || 15 - b_cpu == 4 || 15 - c_cpu == 4 || 15 - d_cpu == 4 || 15 - e_cpu == 4 || 15 - f_cpu == 4 || 15 - g_cpu == 4 || 15 - h_cpu == 4 || 15 - i_cpu == 4 || 15 - j_cpu == 4)) {
                Log.d("cpuwanna", "winby 4");
                winpoint_cpu[get_cpu] = 4;
                if (player1first) {
                    iv_pic[4].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[4].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            } else if ((iv_pic[5].getDrawable() == null) && (15 - a_cpu == 5 || 15 - b_cpu == 5 || 15 - c_cpu == 5 || 15 - d_cpu == 5 || 15 - e_cpu == 5 || 15 - f_cpu == 5 || 15 - g_cpu == 5 || 15 - h_cpu == 5 || 15 - i_cpu == 5 || 15 - j_cpu == 5)) {
                Log.d("cpuwanna", "winby 5");
                winpoint_cpu[get_cpu] = 5;
                if (player1first) {
                    iv_pic[5].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[5].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            } else if ((iv_pic[6].getDrawable() == null) && (15 - a_cpu == 6 || 15 - b_cpu == 6 || 15 - c_cpu == 6 || 15 - d_cpu == 6 || 15 - e_cpu == 6 || 15 - f_cpu == 6 || 15 - g_cpu == 6 || 15 - h_cpu == 6 || 15 - i_cpu == 6 || 15 - j_cpu == 6)) {
                Log.d("cpuwanna", "winby 6");
                winpoint_cpu[get_cpu] = 6;
                if (player1first) {
                    iv_pic[6].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[6].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            } else if ((iv_pic[7].getDrawable() == null) && (15 - a_cpu == 7 || 15 - b_cpu == 7 || 15 - c_cpu == 7 || 15 - d_cpu == 7 || 15 - e_cpu == 7 || 15 - f_cpu == 7 || 15 - g_cpu == 7 || 15 - h_cpu == 7 || 15 - i_cpu == 7 || 15 - j_cpu == 7)) {
                Log.d("cpuwanna", "winby 7");
                winpoint_cpu[get_cpu] = 7;
                if (player1first) {
                    iv_pic[7].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[7].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            } else if ((iv_pic[8].getDrawable() == null) && (15 - a_cpu == 8 || 15 - b_cpu == 8 || 15 - c_cpu == 8 || 15 - d_cpu == 8 || 15 - e_cpu == 8 || 15 - f_cpu == 8 || 15 - g_cpu == 8 || 15 - h_cpu == 8 || 15 - i_cpu == 8 || 15 - j_cpu == 8)) {
                Log.d("cpuwanna", "winby 8");
                winpoint_cpu[get_cpu] = 8;
                if (player1first) {
                    iv_pic[8].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[8].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            } else if ((iv_pic[9].getDrawable() == null) && (15 - a_cpu == 9 || 15 - b_cpu == 9 || 15 - c_cpu == 9 || 15 - d_cpu == 9 || 15 - e_cpu == 9 || 15 - f_cpu == 9 || 15 - g_cpu == 9 || 15 - h_cpu == 9 || 15 - i_cpu == 9 || 15 - j_cpu == 9)) {
                Log.d("cpuwanna", "winby 9");
                winpoint_cpu[get_cpu] = 9;
                if (player1first) {
                    iv_pic[9].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[9].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            }
            //如果對手在一排中已經佔有兩個格子，在接續的棋步必須下在剩餘的另一格空格中，才不會輸。
            else if ((iv_pic[1].getDrawable() == null) && (15 - a_player == 1 || 15 - b_player == 1 || 15 - c_player == 1 || 15 - d_player == 1 || 15 - e_player == 1 || 15 - f_player == 1 || 15 - g_player == 1 || 15 - h_player == 1 || 15 - i_player == 1 || 15 - j_player == 1)) {
                Log.d("cpudont", "loseby 1");
                winpoint_cpu[get_cpu] = 1;
                if (player1first) {
                    iv_pic[1].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[1].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            } else if ((iv_pic[2].getDrawable() == null) && (15 - a_player == 2 || 15 - b_player == 2 || 15 - c_player == 2 || 15 - d_player == 2 || 15 - e_player == 2 || 15 - f_player == 2 || 15 - g_player == 2 || 15 - h_player == 2 || 15 - i_player == 2 || 15 - j_player == 2)) {
                Log.d("cpudont", "loseby 2");
                winpoint_cpu[get_cpu] = 2;
                if (player1first) {
                    iv_pic[2].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[2].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            } else if ((iv_pic[3].getDrawable() == null) && (15 - a_player == 3 || 15 - b_player == 3 || 15 - c_player == 3 || 15 - d_player == 3 || 15 - e_player == 3 || 15 - f_player == 3 || 15 - g_player == 3 || 15 - h_player == 3 || 15 - i_player == 3 || 15 - j_player == 3)) {
                Log.d("cpudont", "loseby 3");
                winpoint_cpu[get_cpu] = 3;
                if (player1first) {
                    iv_pic[3].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[3].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            } else if ((iv_pic[4].getDrawable() == null) && (15 - a_player == 4 || 15 - b_player == 4 || 15 - c_player == 4 || 15 - d_player == 4 || 15 - e_player == 4 || 15 - f_player == 4 || 15 - g_player == 4 || 15 - h_player == 4 || 15 - i_player == 4 || 15 - j_player == 4)) {
                Log.d("cpudont", "loseby 4");
                winpoint_cpu[get_cpu] = 4;
                if (player1first) {
                    iv_pic[4].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[4].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            } else if ((iv_pic[5].getDrawable() == null) && (15 - a_player == 5 || 15 - b_player == 5 || 15 - c_player == 5 || 15 - d_player == 5 || 15 - e_player == 5 || 15 - f_player == 5 || 15 - g_player == 5 || 15 - h_player == 5 || 15 - i_player == 5 || 15 - j_player == 5)) {
                Log.d("cpudont", "loseby 5");
                winpoint_cpu[get_cpu] = 5;
                if (player1first) {
                    iv_pic[5].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[5].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            } else if ((iv_pic[6].getDrawable() == null) && (15 - a_player == 6 || 15 - b_player == 6 || 15 - c_player == 6 || 15 - d_player == 6 || 15 - e_player == 6 || 15 - f_player == 6 || 15 - g_player == 6 || 15 - h_player == 6 || 15 - i_player == 6 || 15 - j_player == 6)) {
                Log.d("cpudont", "loseby 6");
                winpoint_cpu[get_cpu] = 6;
                if (player1first) {
                    iv_pic[6].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[6].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            } else if ((iv_pic[7].getDrawable() == null) && (15 - a_player == 7 || 15 - b_player == 7 || 15 - c_player == 7 || 15 - d_player == 7 || 15 - e_player == 7 || 15 - f_player == 7 || 15 - g_player == 7 || 15 - h_player == 7 || 15 - i_player == 7 || 15 - j_player == 7)) {
                Log.d("cpudont", "loseby 7");
                winpoint_cpu[get_cpu] = 7;
                if (player1first) {
                    iv_pic[7].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[7].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            } else if ((iv_pic[8].getDrawable() == null) && (15 - a_player == 8 || 15 - b_player == 8 || 15 - c_player == 8 || 15 - d_player == 8 || 15 - e_player == 8 || 15 - f_player == 8 || 15 - g_player == 8 || 15 - h_player == 8 || 15 - i_player == 8 || 15 - j_player == 8)) {
                Log.d("cpudont", "loseby 8");
                winpoint_cpu[get_cpu] = 8;
                if (player1first) {
                    iv_pic[8].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[8].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            } else if ((iv_pic[9].getDrawable() == null) && (15 - a_player == 9 || 15 - b_player == 9 || 15 - c_player == 9 || 15 - d_player == 9 || 15 - e_player == 9 || 15 - f_player == 9 || 15 - g_player == 9 || 15 - h_player == 9 || 15 - i_player == 9 || 15 - j_player == 9)) {
                Log.d("cpudont", "loseby 9");
                winpoint_cpu[get_cpu] = 9;
                if (player1first) {
                    iv_pic[9].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[9].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            }
            //如果自己有雙頭蛇機會，先下在那。
            else if ((a_player == 5 && a_cpu == 11 && iv_pic[2].getDrawable() == null) || (a_player == 9 && a_cpu == 9 && iv_pic[2].getDrawable() == null)) {
                Log.d("cpu have ", "chance 2");
                winpoint_cpu[get_cpu] = 2;
                if (player1first) {
                    iv_pic[2].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[2].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            }
            else if ((a_player == 3 && a_cpu == 13 && iv_pic[4].getDrawable() == null) || (a_player == 15 && a_cpu == 7 && iv_pic[4].getDrawable() == null)) {
                Log.d("cpu have ", "chance 4");
                winpoint_cpu[get_cpu] = 4;
                if (player1first) {
                    iv_pic[4].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[4].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            }
            else if ((a_player == 5 && a_cpu == 13 && iv_pic[6].getDrawable() == null) || (a_player == 17 && a_cpu == 7 && iv_pic[6].getDrawable() == null)) {
                Log.d("cpu have ", "chance 6");
                winpoint_cpu[get_cpu] = 6;
                if (player1first) {
                    iv_pic[6].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[6].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            }
            else if ((a_player == 11 && a_cpu == 11 && iv_pic[8].getDrawable() == null) || (a_player == 15 && a_cpu == 9 && iv_pic[8].getDrawable() == null)) {
                Log.d("cpu have ", "chance 8");
                winpoint_cpu[get_cpu] = 8;
                if (player1first) {
                    iv_pic[8].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[8].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            }
            else if ((winpoint_player1[0] + winpoint_player1[1] + winpoint_player1[2] == 17) && (a_cpu == 8) && (iv_pic[4].getDrawable() == null && iv_pic[8].getDrawable() == null)) {
                Log.d("cpu have ", "chance 4 or 8");
                int[] ran_tt = {4 , 8};
                int ran_two = (int)(Math.random() * 1);
                winpoint_cpu[get_cpu] = ran_tt[ran_two];
                if (player1first) {
                    iv_pic[ran_tt[ran_two]].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[ran_tt[ran_two]].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            }
            else if ((winpoint_player1[0] + winpoint_player1[1] + winpoint_player1[2] == 11) && (a_cpu == 14) && (iv_pic[2].getDrawable() == null && iv_pic[4].getDrawable() == null)) {
                Log.d("cpu have ", "chance 2 or 4");
                int[] ran_tt = {2 , 4};
                int ran_two = (int)(Math.random() * 1);
                winpoint_cpu[get_cpu] = ran_tt[ran_two];
                if (player1first) {
                    iv_pic[ran_tt[ran_two]].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[ran_tt[ran_two]].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            }
            else if ((winpoint_player1[0] + winpoint_player1[1] + winpoint_player1[2] == 13) && (a_cpu == 12) && (iv_pic[2].getDrawable() == null && iv_pic[6].getDrawable() == null)) {
                Log.d("cpu have ", "chance 2 or 6");
                int[] ran_tt = {2 , 6};
                int ran_two = (int)(Math.random() * 1);
                winpoint_cpu[get_cpu] = ran_tt[ran_two];
                if (player1first) {
                    iv_pic[ran_tt[ran_two]].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[ran_tt[ran_two]].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            }
            else if ((winpoint_player1[0] + winpoint_player1[1] + winpoint_player1[2] == 19) && (a_cpu == 6) && (iv_pic[6].getDrawable() == null && iv_pic[8].getDrawable() == null)) {
                Log.d("cpu have ", "chance 6 or 8");
                int[] ran_tt = {6 , 8};
                int ran_two = (int)(Math.random() * 1);
                winpoint_cpu[get_cpu] = ran_tt[ran_two];
                if (player1first) {
                    iv_pic[ran_tt[ran_two]].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[ran_tt[ran_two]].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            }
            //如果中心是空的，就下在那裡。
            else if (iv_pic[5].getDrawable() == null) {
                Log.d("the middle", "is Empty 5");
                winpoint_cpu[get_cpu] = 5;
                if (player1first) {
                    iv_pic[5].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[5].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            }
            //如果對手下在角落，自己就下再與他角落相對的位置。
            else if (winpoint_player1[get_player1 - 1] == 2 && iv_pic[8].getDrawable() == null) {
                Log.d("cpu think player", "corner 8");
                winpoint_cpu[get_cpu] = 8;
                if (player1first) {
                    iv_pic[8].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[8].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            }
            else if (winpoint_player1[get_player1 - 1] == 4 && iv_pic[6].getDrawable() == null) {
                Log.d("cpu think player", "corner 6");
                winpoint_cpu[get_cpu] = 6;
                if (player1first) {
                    iv_pic[6].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[6].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            }
            else if (winpoint_player1[get_player1 - 1] == 6 && iv_pic[4].getDrawable() == null) {
                Log.d("cpu think player", "corner 4");
                winpoint_cpu[get_cpu] = 4;
                if (player1first) {
                    iv_pic[4].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[4].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            }
            else if (winpoint_player1[get_player1 - 1] == 8 && iv_pic[2].getDrawable() == null) {
                Log.d("cpu think player", "corner 2");
                winpoint_cpu[get_cpu] = 2;
                if (player1first) {
                    iv_pic[2].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[2].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            }
            //如果自己下在中間位置，對手下在相對的兩個角位，自己就要下在其他邊位。
            else if (iv_pic[ran_side].getDrawable() != null && a_player == 10 && (iv_pic[1].getDrawable() == null || iv_pic[3].getDrawable() == null || iv_pic[5].getDrawable() == null || iv_pic[7].getDrawable() == null)) {
                Log.d("Random_side", "again" + ran_side);
                int ran_again[] = new int[1];
                for (int r = 0; r < 1; r++) {
                    ran_again[r] = (int) (Math.random() * 4 + 1) * 2 - 1;
                    if (ran_again[r] != ran_side && iv_pic[ran_again[r]].getDrawable() == null) {
                        Log.d("Random_side", ran_again[r] + "");
                        winpoint_cpu[get_cpu] = ran_again[r];
                        if (player1first) {
                            iv_pic[ran_again[r]].setImageResource(R.drawable.cross);
                        } else {
                            iv_pic[ran_again[r]].setImageResource(R.drawable.circle);
                        }
                        get_cpu++;
                        changeside = true;
                    } else {
                        r--;
                    }
                }
            } else if (iv_pic[ran_side].getDrawable() == null && a_player == 10 && (iv_pic[1].getDrawable() == null || iv_pic[3].getDrawable() == null || iv_pic[5].getDrawable() == null || iv_pic[7].getDrawable() == null)) {
                Log.d("Random_side", "once " + ran_side);
                winpoint_cpu[get_cpu] = ran_side;
                if (player1first) {
                    iv_pic[ran_side].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[ran_side].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            }
            //如果有角落有空格，先下在角落的空格。
            else if (iv_pic[ran_corner].getDrawable() != null && (iv_pic[2].getDrawable() == null || iv_pic[4].getDrawable() == null || iv_pic[6].getDrawable() == null || iv_pic[8].getDrawable() == null)) {
                Log.d("Random_corner", "again" + ran_corner);
                int ran_again[] = new int[1];
                for (int r = 0; r < 1; r++) {
                    ran_again[r] = (int) (Math.random() * 4 + 1) * 2;
                    if (ran_again[r] != ran_corner && iv_pic[ran_again[r]].getDrawable() == null) {
                        Log.d("Random_corner", ran_again[r] + "");
                        winpoint_cpu[get_cpu] = ran_again[r];
                        if (player1first) {
                            iv_pic[ran_again[r]].setImageResource(R.drawable.cross);
                        } else {
                            iv_pic[ran_again[r]].setImageResource(R.drawable.circle);
                        }
                        get_cpu++;
                        changeside = true;
                    } else {
                        r--;
                    }
                }
            } else if (iv_pic[ran_corner].getDrawable() == null && (iv_pic[2].getDrawable() == null || iv_pic[4].getDrawable() == null || iv_pic[6].getDrawable() == null || iv_pic[8].getDrawable() == null)) {
                Log.d("Random_corner", "once " + ran_corner);
                winpoint_cpu[get_cpu] = ran_corner;
                if (player1first) {
                    iv_pic[ran_corner].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[ran_corner].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            }
            //如果有空格，就隨機下在一個空格中。
            else if (iv_pic[ran].getDrawable() != null) {
                Log.d("Random", "again");
                int ran_again[] = new int[1];
                for (int r = 0; r < 1; r++) {
                    ran_again[r] = (int) (Math.random() * 9 + 1);
                    if (iv_pic[ran_again[r]].getDrawable() == null) {
                        Log.d("Random", ran_again[r] + "");
                        winpoint_cpu[get_cpu] = ran_again[r];
                        if (player1first) {
                            iv_pic[ran_again[r]].setImageResource(R.drawable.cross);
                        } else {
                            iv_pic[ran_again[r]].setImageResource(R.drawable.circle);
                        }
                        get_cpu++;
                        changeside = true;
                    } else {
                        r--;
                    }
                }

            } else if (iv_pic[ran].getDrawable() == null) {
                Log.d("Random", "once " + ran);
                winpoint_cpu[get_cpu] = ran;
                if (player1first) {
                    iv_pic[ran].setImageResource(R.drawable.cross);
                } else {
                    iv_pic[ran].setImageResource(R.drawable.circle);
                }
                get_cpu++;
                changeside = true;
            }


        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
