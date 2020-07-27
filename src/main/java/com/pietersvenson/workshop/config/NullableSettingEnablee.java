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

package com.pietersvenson.workshop.config;

import com.pietersvenson.workshop.features.Enablee;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class NullableSettingEnablee implements Enablee {

  Setting<Boolean> enablerSetting;

  protected NullableSettingEnablee(@Nullable Setting<Boolean> enablerSetting) {
    this.enablerSetting = enablerSetting;
  }

  public boolean hasSetting() {
    return enablerSetting != null;
  }

  @Override
  public boolean isEnabled() {
    return Optional.ofNullable(enablerSetting).map(Setting::getValue).orElse(true);
  }

  public void setEnabler(@Nullable Setting<Boolean> enablerSetting) {
    this.enablerSetting = enablerSetting;
  }

  @Override
  public final boolean enable() {
    if (enablerSetting == null) {
      return false;
    }
    if (enablerSetting.getValue()) {
      return false;
    }
    enablerSetting.setValue(true);
    return true;
  }

  @Override
  public final boolean disable() {
    if (enablerSetting == null) {
      return false;
    }
    if (!enablerSetting.getValue()) {
      return false;
    }
    enablerSetting.setValue(false);
    return true;
  }

}
