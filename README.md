# StrictConfig

Utilities for less tolerant Clojure service configuration maps.

> This library builds heavily on the [strictly](https://github.com/ctford/strictly).

## Background

Service configuration for an environment often uses configuration files,
templated using provisioners such as [chef](https://chef.io) or
[ansible](https://ansible.com). Missing configuration attributes may not be
exposed until service deployment, during runtime access, or may be obscured
behind a lack of checks within the service.

This library helps expose missing configuration attributes by failing fast
(thowing a `RuntimeException`) when invalid configuration is presented.

## Usage

If you want to use it, include it in your `project.clj` file to download it
from Clojars:

    [strict-config "0.1.0-SNAPSHOT"]

Apply the `strict` function from the `map` namespace to create a map that
throws an Exception if a key that does not exist is retrieved. This is in
contrast to Clojure's default behaviour of returning `nil` in such cases.

The `strict` function will throw an Exception if the map contains nil values.

See [the tests](https://github.com/brydoncheyney/strict-config/blob/master/test/strict-confg/map_test.clj)
for example usages.

## Credits

- [Chris Ford](https://github.com/ctford)
- [Francois Terrier](https://github.com/fterrier)

## Licence

Copyright © 2017 Brydon Cheyney
