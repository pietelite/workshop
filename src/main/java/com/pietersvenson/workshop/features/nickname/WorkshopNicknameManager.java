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

package com.pietersvenson.workshop.features.nickname;

import com.google.common.collect.Maps;
import com.pietersvenson.workshop.features.FeatureListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class WorkshopNicknameManager extends NicknameManager {

  HashMap<UUID, String> nicknames = Maps.newHashMap();

  @Override
  boolean hasNickname(@Nonnull UUID playerUuid) throws UnsupportedOperationException {
    return nicknames.containsKey(playerUuid);
  }

  @Override
  boolean isNicknameUsed(@Nonnull String nick) throws UnsupportedOperationException {
    return nicknames.containsValue(nick);
  }

  @Nonnull
  @Override
  public Optional<String> getNickname(@Nonnull UUID playerUuid) throws UnsupportedOperationException {
    return Optional.ofNullable(nicknames.get(playerUuid));
  }

  @Override
  public void setNickname(@Nonnull UUID playerUuid, @Nonnull String nick) throws UnsupportedOperationException {
    nicknames.put(playerUuid, nick);
    Bukkit.getPlayer(playerUuid).setDisplayName("-" + nick);
    Bukkit.getPlayer(playerUuid).setCustomName("-");
  }

  @Override
  void removeNickname(@Nonnull UUID playerUuid) throws UnsupportedOperationException {
    nicknames.remove(playerUuid);
    Player player = Bukkit.getPlayer(playerUuid);
    player.setDisplayName(player.getName());
    player.setPlayerListName(player.getName());
  }

  @Nonnull
  @Override
  protected Collection<FeatureListener> getListeners() {
    return super.getListeners();
  }
}
