package com.trophonix.tradeplus.logging;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.trophonix.tradeplus.TradePlus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.UnaryOperator;

import org.bukkit.inventory.ItemStack;

public class Logs implements List<TradeLog> {

  private static final DateFormat folderNameFormat =
      new SimpleDateFormat("'session_'yyyy-MM-dd'_'HH:mm:ss");

  private static final DateFormat fileNameFormat =
      new SimpleDateFormat("'{player1}-{player2}_'HH:mm:ss'.json'");

  private TradePlus plugin;

  private File folder;
  private List<TradeLog> logs = new ArrayList<>();

  private Gson gson = new GsonBuilder().setPrettyPrinting().create();

  public Logs(TradePlus plugin, File parent, String file) {
    this.plugin = plugin;
    if (!parent.exists()) {
      parent.mkdirs();
    }
    folder = parent;
  }

  public Logs(TradePlus plugin, File parent) {
    this(plugin, parent, folderNameFormat.format(new Date()));
  }

  public void log(TradeLog log) {
    logs.add(log);
    save();
  }

  public void save() {
    try {
      if (!logs.isEmpty()) {
        if (!folder.exists()) folder.mkdirs();
        Iterator<TradeLog> iter = iterator();
        while (iter.hasNext()) {
          TradeLog log = iter.next();
          try {
            File file =
                new File(
                    folder,
                    fileNameFormat
                        .format(log.getTime())
                        .replace("{player1}", log.getPlayer1().getLastKnownName())
                        .replace("{player2}", log.getPlayer2().getLastKnownName()));
            if (!file.exists()) file.createNewFile();
            FileWriter writer = new FileWriter(file);
            JsonObject obj = new JsonObject();

            obj.addProperty("date", log.getTime().toString());
            obj.add(log.getPlayer1().getLastKnownName()+"-gave-items", log.getPlayer1ItemStacks());
            obj.add(log.getPlayer2().getLastKnownName()+"-gave-items", log.getPlayer2ItemStacks());
            obj.add(log.getPlayer1().getLastKnownName()+"-gave-extra", log.getPlayer1ExtraOffers());
            obj.add(log.getPlayer2().getLastKnownName()+"-gave-extra", log.getPlayer2ExtraOffers());
            gson.toJson(obj, writer);
            writer.close();
          } catch (Exception | Error ex) {
            plugin.getLogger().warning(
                "Failed to save trade log for trade between "
                    + log.getPlayer1().getLastKnownName()
                    + " and "
                    + log.getPlayer2().getLastKnownName());
            ex.printStackTrace();
          }
          iter.remove();
        }
      }
    } catch (Exception | Error ex) {
      plugin.getLogger().warning("Failed to save trade logs.");
      logs.clear();
    }
  }

  @Override
  public int size() {
    return logs.size();
  }

  @Override
  public boolean isEmpty() {
    return logs.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return logs.contains(o);
  }

  @Override
  public Iterator<TradeLog> iterator() {
    return logs.iterator();
  }

  @Override
  public Object[] toArray() {
    return logs.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return logs.toArray(a);
  }

  @Override
  public boolean add(TradeLog tradeLog) {
    return logs.add(tradeLog);
  }

  @Override
  public boolean remove(Object o) {
    return logs.remove(o);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return logs.containsAll(c);
  }

  @Override
  public boolean addAll(Collection<? extends TradeLog> c) {
    return logs.addAll(c);
  }

  @Override
  public boolean addAll(int index, Collection<? extends TradeLog> c) {
    return logs.addAll(index, c);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return logs.removeAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return logs.retainAll(c);
  }

  @Override
  public void replaceAll(UnaryOperator<TradeLog> operator) {
    logs.replaceAll(operator);
  }

  @Override
  public void sort(Comparator<? super TradeLog> c) {
    logs.sort(c);
  }

  @Override
  public void clear() {
    logs.clear();
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    return logs.equals(o);
  }

  @Override
  public int hashCode() {
    return logs.hashCode();
  }

  @Override
  public TradeLog get(int index) {
    return logs.get(index);
  }

  @Override
  public TradeLog set(int index, TradeLog element) {
    return logs.set(index, element);
  }

  @Override
  public void add(int index, TradeLog element) {
    logs.add(index, element);
  }

  @Override
  public TradeLog remove(int index) {
    return logs.remove(index);
  }

  @Override
  public int indexOf(Object o) {
    return logs.indexOf(o);
  }

  @Override
  public int lastIndexOf(Object o) {
    return logs.lastIndexOf(o);
  }

  @Override
  public ListIterator<TradeLog> listIterator() {
    return logs.listIterator();
  }

  @Override
  public ListIterator<TradeLog> listIterator(int index) {
    return logs.listIterator(index);
  }

  @Override
  public List<TradeLog> subList(int fromIndex, int toIndex) {
    return logs.subList(fromIndex, toIndex);
  }

  @Override
  public Spliterator<TradeLog> spliterator() {
    return logs.spliterator();
  }
}
