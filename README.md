Automatic Stub Generator [![Build Status](https://travis-ci.org/williamwebb/auto-stub.svg?branch=master)](https://travis-ci.org/williamwebb/auto-stub)
============

Usage
=====
```
@AutoStub({
    @Stub(MyClass.class) // class to stub
})
public class Stubs { }
```

Config
=====
```
    // source project must be provided and apt scope
    provided 'source-project'
    apt 'source-project'

    // auto-stub
    provided 'autostub-annotations'
    apt 'autostub-compiler'
```
License
-------

    Copyright 2016 William Webb

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
