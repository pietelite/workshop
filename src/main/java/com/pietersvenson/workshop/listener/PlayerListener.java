/*
 * MIT License
 *
 * Copyright (c) 2020 Pieter Svenson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.pietersvenson.workshop.listener;

import com.pietersvenson.workshop.Workshop;
import com.pietersvenson.workshop.util.Format;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerListener implements Listener {

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent playerMoveEvent) {
    if (Workshop.getInstance().getState().getFreezeManager().isFrozen(playerMoveEvent.getPlayer())) {
      playerMoveEvent.setCancelled(true);
    }
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
    if (Workshop.getInstance().getState().getFreezeManager().isAllFrozen()) {
      Workshop.getInstance().getState().getFreezeManager().freeze(playerJoinEvent.getPlayer());
    }
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
    if (Workshop.getInstance().getState().getFreezeManager().isFrozen(playerInteractEvent.getPlayer())) {
      playerInteractEvent.setCancelled(true);
    }
  }

  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent playerChatEvent) {
    if (Workshop.getInstance().getState().getFreezeManager().isFrozen(playerChatEvent.getPlayer())) {
      playerChatEvent.setCancelled(true);
      playerChatEvent.getPlayer().sendMessage(Format.error("You can't chat when you are frozen!"));
    }
  }

}