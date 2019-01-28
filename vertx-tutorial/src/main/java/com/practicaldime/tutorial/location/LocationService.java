package com.practicaldime.tutorial.location;

import java.util.List;

import com.practicaldime.tutorial.entity.Location;

import io.vertx.core.Future;

public interface LocationService {

	Future<Boolean> initData(); // init the data (or table)

	Future<Location> insert(Location todo);

	Future<List<Location>> getAll();

	Future<Location> getCertain(String id);

	Future<Location> update(String id, Location newLoc);

	Future<Boolean> delete(String id);

	Future<Boolean> deleteAll();
}
