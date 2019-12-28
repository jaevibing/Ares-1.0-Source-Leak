package com.ares.commands;

import com.ares.utils.chat.ChatUtils;
import com.ares.utils.data.FriendUtils;

public class Friend extends CommandBase
{
    private String[] options;
    
    public Friend() {
        super(new String[] { "friend", "friends", "f" });
        this.options = new String[] { "add", "del", "list" };
    }
    
    @Override
    public boolean execute(final String[] v0) {
        /*SL:35*/if (!this.checkCommandWithOption(v0, this.options, "name")) {
            return false;
        }
        /*SL:37*/if (v0[0].equals("add") && /*EL:39*/!v0[v0.length - 1].equals("add")) {
            /*SL:41*/if (!FriendUtils.isFriend(v0[1])) {
                /*SL:43*/FriendUtils.addFriend(v0[1]);
                /*SL:44*/ChatUtils.printMessage("Added '" + v0[1] + "' to your friends!", "green");
            }
            else {
                /*SL:47*/ChatUtils.printMessage("'" + v0[1] + "' was already a friend", "red");
            }
            /*SL:49*/return true;
        }
        /*SL:53*/if (v0[0].equals("del") && /*EL:55*/!v0[v0.length - 1].equals("del")) {
            /*SL:57*/if (FriendUtils.isFriend(v0[1])) {
                /*SL:59*/FriendUtils.removeFriend(v0[1]);
                /*SL:60*/ChatUtils.printMessage("Removed '" + v0[1] + "' from your friends!", "green");
            }
            else {
                /*SL:63*/ChatUtils.printMessage("'" + v0[1] + "' wasnt a friend", "red");
            }
            /*SL:65*/return true;
        }
        /*SL:69*/if (v0[0].equals("list")) {
            final StringBuilder v = /*EL:71*/new StringBuilder("Friends: ");
            /*SL:72*/for (int a1 = 0; a1 <= FriendUtils.getFriends().size() - 1; ++a1) {
                /*SL:74*/if (a1 == FriendUtils.getFriends().size() - 1) {
                    /*SL:76*/v.append(FriendUtils.getFriends().get(a1));
                    /*SL:77*/break;
                }
                /*SL:79*/v.append(FriendUtils.getFriends().get(a1)).append(", ");
            }
            /*SL:82*/ChatUtils.printMessage(v.toString(), "red");
            /*SL:83*/return true;
        }
        /*SL:85*/return false;
    }
    
    @Override
    public String getSyntax() {
        /*SL:91*/return "-friend add cookiedragon234\n-friend del 2b2tnews\n-friend list";
    }
}
