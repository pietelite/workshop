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

package com.pietersvenson.workshop.command.common;

import com.google.common.collect.Lists;
import com.pietersvenson.workshop.Workshop;
import com.pietersvenson.workshop.features.classes.Curriculum;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class ParameterSuppliers {

  private ParameterSuppliers() {
  }

  public static final Parameter.ParameterSupplier NONE = Parameter.ParameterSupplier.builder()
      .allowedEntries(Lists::newLinkedList)
      .usage("")
      .build();

  public static final Parameter.ParameterSupplier ONLINE_PLAYER = Parameter.ParameterSupplier.builder()
      .allowedEntries(prev ->
          Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()))
      .usage("<player>")
      .strict(false)
      .build();

  public static final Parameter.ParameterSupplier BOOLEAN = Parameter.ParameterSupplier.builder()
      .allowedEntries(prev ->
          Lists.newArrayList("true", "false", "t", "f"))
      .usage("true|false")
      .build();

  public static final Parameter.ParameterSupplier ITEM = Parameter.ParameterSupplier.builder()
      .allowedEntries(prev -> {
        List<String> out = Lists.newLinkedList();
        Lists.newLinkedList(Registry.MATERIAL)
            .stream()
            .filter(Material::isItem)
            .forEach(material -> {
              out.add(material.toString().toLowerCase());
              out.add(material.getKey().toString());
            });
        return out;
      })
      .usage("<item>")
      .build();

  public static final Parameter.ParameterSupplier CURRICULUM = Parameter.ParameterSupplier.builder()
      .allowedEntries(prev -> Arrays.stream(Curriculum.values())
          .map(curriculum -> curriculum.name().toLowerCase())
          .collect(Collectors.toList()))
      .usage("<curriculum>")
      .build();

  public static final Parameter.ParameterSupplier CLASS_ID = Parameter.ParameterSupplier.builder()
      .allowedEntries(prev ->
          Workshop.getInstance().getState().getClassroomManager().getClassroomIds())
      .usage("<class>")
      .build();

  public static final Parameter.ParameterSupplier CLASS_START_DATE = Parameter.ParameterSupplier.builder()
      .allowedEntries(prev -> IntStream.range(-10, 30)
          .mapToObj(integer ->
              new SimpleDateFormat("YY/MM/dd").format(Date.from(Instant.now().plus(Duration.of(integer, ChronoUnit.DAYS)))))
          .collect(Collectors.toList()))
      .usage("<start-date>")
      .strict(false)
      .build();

  public static final Parameter.ParameterSupplier CLASS_END_DATE = Parameter.ParameterSupplier.builder()
      .allowedEntries(prev -> IntStream.range(-10, 30)
          .mapToObj(integer ->
              new SimpleDateFormat("YY/MM/dd").format(Date.from(Instant.now().plus(Duration.of(integer, ChronoUnit.DAYS)))))
          .collect(Collectors.toList()))
      .usage("<end-date>")
      .strict(false)
      .build();

  public static final Parameter.ParameterSupplier CLASS_START_TIME = Parameter.ParameterSupplier.builder()
      .allowedEntries(prev -> {
        List<String> out = Lists.newLinkedList();
        for (int i = 0; i < 48; i++) {
          DecimalFormat df = new DecimalFormat("00");
          out.add(df.format(i / 2) + ":" + df.format((i % 2) * 30));
        }
        return out;
      })
      .usage("<start-time>")
      .strict(false)
      .build();

  public static final Parameter.ParameterSupplier CLASS_END_TIME = Parameter.ParameterSupplier.builder()
      .allowedEntries(prev -> {
        List<String> out = Lists.newLinkedList();
        for (int i = 0; i < 48; i++) {
          DecimalFormat df = new DecimalFormat("00");
          out.add(df.format(i / 2) + ":" + df.format((i % 2) * 30));
        }
        return out;
      })
      .usage("<end-time>")
      .strict(false)
      .build();

}
