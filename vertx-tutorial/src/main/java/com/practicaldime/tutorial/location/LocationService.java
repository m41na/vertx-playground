package com.practicaldime.tutorial.location;

import java.util.List;

import com.practicaldime.tutorial.entity.Location;

import io.vertx.core.Future;

public interface LocationService {

	Future<Void> initData(); // init the data (or table)

	Future<Location> insert(Location location);

	Future<List<Location>> getAll();

	Future<Location> getCertain(String id);

	Future<Void> update(String id, Location location);

	Future<Void> delete(String id);

	Future<Boolean> deleteAll();
}
