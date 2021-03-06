/**
 * event-bus - An event bus framework for event driven programming
 * Copyright (C) 2016  Adrian Cristian Ionescu - https://github.com/acionescu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.segoia.event.conditions;

import net.segoia.event.eventbus.EventContext;

public class NotCondition extends AggregatedCondition{

    public NotCondition(String id, Condition[] subconditions) {
	super(id, subconditions);
    }
    
    public NotCondition(String id, Condition cond){
	super(id,new Condition[]{cond});
    }
    
    public NotCondition(Condition cond) {
	this("!"+cond.getId(),cond);
    }
    

    @Override
    public boolean test(EventContext input) {
	if (subconditions == null || subconditions.length == 0) {
	    return true;
	}
	/* negating the result of the first condition */
	return !subconditions[0].test(input);
    }

}
